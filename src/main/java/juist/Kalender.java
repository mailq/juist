package juist;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.TimeZone;
import net.fortuna.ical4j.model.TimeZoneRegistryFactory;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.Geo;
import net.fortuna.ical4j.model.property.Location;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Version;
import net.fortuna.ical4j.util.RandomUidGenerator;
import net.fortuna.ical4j.util.UidGenerator;

public class Kalender {
  public static Calendar alsKalender(List<LocalDateTime> fahrten) {
    // Create a TimeZone
    var registry = TimeZoneRegistryFactory.getInstance().createRegistry();
    var timezone = registry.getTimeZone("Europe/Berlin");

    var calendar = new Calendar();
    calendar.getProperties().add(new ProdId("-//Ben Fortuna//iCal4j 3.2.8//DE"));
    calendar.getProperties().add(Version.VERSION_2_0);
    calendar.getProperties().add(CalScale.GREGORIAN);
    calendar.getProperties().add(new Description(
        "Extrahiert von https://www.inselfaehre.de/ Download Fahrplan Juist PDF. Alle Angaben ohne Gewähr. An manchen Tagen kann die Fähre von einem anderen Fähranleger ablegen. Je nach Nachfrage können auch zusätzliche Vorfähren früher abfahren."));

    var location = new Location("Fährbrücke 1, Mole Norddeich 1, 26506 Norden");
    var coordinates =
        new Geo(BigDecimal.valueOf(53.6235346509309), BigDecimal.valueOf(7.157721485059674));
    var erzeugtAm = fahrten.iterator().next();
    var stamp = new DateTime(java.sql.Date.valueOf(erzeugtAm.toLocalDate()), timezone);

    // Add events, etc..
    var components = calendar.getComponents();
    for (LocalDateTime zeit : fahrten) {
      var event = alsEvent(timezone, zeit);
      event.getProperties().add(location);
      event.getProperties().add(coordinates);
      event.getDateStamp().setDateTime(stamp);
      components.add(event);
    }
    return calendar;
  }

  private static VEvent alsEvent(TimeZone timezone, LocalDateTime zeit) {
    var startZeit = java.sql.Timestamp.valueOf(zeit);
    var endeZeit = java.sql.Timestamp.valueOf(zeit.plusMinutes(90));

    // Create the event
    var eventName = "Norddeich -> Juist";
    var start = new DateTime(startZeit, timezone);
    var end = new DateTime(endeZeit, timezone);
    var meeting = new VEvent(start, end, eventName);
    UidGenerator ug = new RandomUidGenerator();
    var uid = ug.generateUid();
    meeting.getProperties().add(uid);
    return meeting;
  }
}
