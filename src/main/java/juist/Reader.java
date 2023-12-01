package juist;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Predicate;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;
import com.google.common.base.Splitter;

public class Reader {
  public final static List<LocalDateTime> extrahiereHinfahrten(File file, Year jahr)
      throws IOException {
    try (var document = Loader.loadPDF(file)) {
      try (var output = new StringWriter();) {
        var spalten = new TreeMap<Integer, List<String>>();
        var stripper = new PDFTextStripper() {
          @Override
          protected void writeString(String text, List<TextPosition> textPositions)
              throws IOException {
            int pos = (int) textPositions.get(0).getX() / 85;
            spalten.putIfAbsent(pos, new ArrayList<>());
            spalten.get(pos).add(text);
            super.writeString(text, textPositions);
          }
        };
        stripper.setEndPage(1);
        stripper.setSortByPosition(true);
        stripper.setWordSeparator("DUMMY");
        stripper.writeText(document, output);
        return extrahiereHinfahrten(jahr, spalten);
      }
    }
  }

  private static List<LocalDateTime> extrahiereHinfahrten(Year jahr,
      Map<Integer, List<String>> spalten) {
    var fahrten = new ArrayList<LocalDateTime>();
    var splitter = Splitter.on(' ').omitEmptyStrings().trimResults();
    for (int monat = 1; monat < 13; monat++) {
      var zeilen = spalten.get(monat - 1).stream().flatMap(l -> splitter.splitToStream(l))
          .map(String::strip).filter(Predicate.not(String::isEmpty)).iterator();
      var zeile = zeilen.next();
      while (!zeile.equals("1."))
        zeile = zeilen.next();

      var tag = Integer.valueOf(zeile.replace(".", ""));

      while (zeilen.hasNext()) {
        zeile = zeilen.next();
        if (zeile.charAt(0) < '0' | zeile.charAt(0) > '9')
          continue;
        if (zeile.endsWith(".")) {
          tag = Integer.valueOf(zeile.replace(".", ""));
          continue;
        }
        var hin = zeile.replace('.', ':');
        var zurueck = zeilen.next();
        var fahrt =
            LocalDateTime.of(LocalDate.of(jahr.getValue(), monat, tag), LocalTime.parse(hin));
        fahrten.add(fahrt);
      }
    }
    return fahrten;
  }
}
