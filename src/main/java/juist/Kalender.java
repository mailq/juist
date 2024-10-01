package juist;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.TimeZone;
import net.fortuna.ical4j.model.TimeZoneRegistryFactory;
import net.fortuna.ical4j.model.component.CalendarComponent;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.DtStamp;
import net.fortuna.ical4j.model.property.Geo;
import net.fortuna.ical4j.model.property.Location;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.TzName;
import net.fortuna.ical4j.model.property.Version;
import net.fortuna.ical4j.model.property.immutable.ImmutableVersion;
import net.fortuna.ical4j.util.RandomUidGenerator;
import net.fortuna.ical4j.util.UidGenerator;

public class Kalender {
  public static Calendar alsKalender(List<LocalDateTime> fahrten) {
    var timezone = new TzName("Europe/Berlin");
    var calendar = new Calendar().withProdId("-//Ben Fortuna//iCal4j 4.0.4//DE")
        .withProperty(ImmutableVersion.VERSION_2_0)
        .withProperty(new CalScale(CalScale.VALUE_GREGORIAN))
        .getFluentTarget();
    calendar.add(new Description(
        "Extrahiert von https://www.inselfaehre.de/ Download Fahrplan Juist PDF. Alle Angaben ohne Gewähr. An manchen Tagen kann die Fähre von einem anderen Fähranleger ablegen. Je nach Nachfrage können auch zusätzliche Vorfähren früher abfahren."));

    var location = new Location("Fährbrücke 1, Mole Norddeich 1, 26506 Norden");
    var coordinates = new Geo(BigDecimal.valueOf(53.6235346509309), BigDecimal.valueOf(7.157721485059674));
    var created = new DtStamp(fahrten.iterator().next().toInstant(ZoneOffset.UTC));

    for (LocalDateTime zeit : fahrten) {
      var event = alsEvent(timezone, zeit);
      event.add(location);
      event.add(coordinates);
      event.replace(created);
      calendar.add(event);
    }
    return calendar;
  }

  private static VEvent alsEvent(TzName timezone, LocalDateTime zeit) {
    var eventName = "Norddeich -> Juist";
    var meeting = new VEvent(zeit, zeit.plusMinutes(90), eventName);
    UidGenerator ug = new RandomUidGenerator();
    var uid = ug.generateUid();
    meeting.add(uid);
    meeting.add(timezone);
    return meeting;
  }
}
