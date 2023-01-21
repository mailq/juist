package juist;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import com.google.common.base.Splitter;
import com.google.common.io.CharSource;

public class Reader {
  public final static List<LocalDateTime> extrahiereHinfahrten(File file) throws IOException {
    try (var document = PDDocument.load(file)) {
      try (var output = new StringWriter();) {
        var stripper = new PDFTextStripper();
        stripper.setSortByPosition(true);
        stripper.writeText(document, output);
        return extrahiereHinfahrten(
            new ArrayList<>(CharSource.wrap(output.getBuffer()).readLines()));
      }
    }
  }

  private static List<LocalDateTime> extrahiereHinfahrten(List<String> lines) {
    // pdftotext --layout
    lines.remove(0);
    lines.remove(0);
    lines.remove(0);
    var splitter = Splitter.on(' ').omitEmptyStrings().trimResults();
    var columns =
        lines.stream().map(l -> splitter.splitToStream(l).collect(Collectors.toList())).toList();
    int i = 0;
    var fahrten = new ArrayList<LocalDateTime>();
    for (LocalDate current = LocalDate.of(2023, 1, 1); current
        .isBefore(LocalDate.of(2024, 1, 1)); current = current.plusDays(1)) {
      if (current.getDayOfMonth() == 1)
        i = 0;
      var line = columns.get(i);

      var wochentag = line.get(0);
      var hin = line.get(2).replace('.', ':');
      LocalDateTime fahrt;
      if (hin.charAt(0) != '-') {
        var time = LocalTime.parse(hin);
        fahrt = current.atTime(time);
        fahrten.add(fahrt);

        for (int j = 0; j < 4; j++)
          line.remove(0);
      } else
        // Erste Fahrt fällt aus
        fahrt = LocalDateTime.MIN;

      // Zweite Zeile?
      i++;
      line = columns.get(i);
      if (line.isEmpty())
        // Gar keine Zeile
        continue;
      wochentag = line.get(0);
      if (wochentag.length() == 2)
        // Schon der nächste Tag
        continue;
      var zweite = line.get(0).replace('.', ':');
      if (zweite.charAt(0) == '-') {
        // Keine Hinfahrt, nur Rückfahrt
        for (int j = 0; j < 2; j++)
          line.remove(0);
        i++;
        continue;
      }
      var fahrt2 = current.atTime(LocalTime.parse(zweite));
      if (fahrt.isAfter(fahrt2))
        // Spezialfall, wenn Information aus Folgemonat kommt;
        // meist fälschlicherweise am Monatsende
        continue;
      fahrten.add(fahrt2);
      for (int j = 0; j < 2; j++)
        line.remove(0);
      i++;
    }
    return fahrten;
  }
}
