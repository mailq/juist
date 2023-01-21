package juist;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class Main {
  public static void main(String[] args) throws IOException {
    System.out.println("Lese PDF " + args[0]);
    System.out.println("Schreibe Kalender " + args[1]);
    var hinfahrten = Reader.extrahiereHinfahrten(new File(args[0]));

    Files.writeString(Path.of(args[1]), Kalender.alsKalender(hinfahrten).toString(),
        StandardCharsets.UTF_8, StandardOpenOption.CREATE_NEW);
    System.out.println("Fertig");
  }
}
