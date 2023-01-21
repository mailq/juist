# Fährplan Juist als Kalender

Die Reederei Frisia (inselfaehre.de) bietet auf ihren Webseiten keinen Kalender zum Download an. Um diesen Misstand zu umgehen, stellt dieses Projekt ein Programm zur Verfügung, diesen Kalender aus dem PDF-Fährplan selbst zu generieren.

Die generierte Datei (ics) im iCalendar-Format kann im Anschluss in viele Kalender-Programme wie Thunderbird, iCal, Evolution oder auch Cloud-Kalender wie Nextcloud, ownCloud, Google Kalender importiert werden.

## Benutzung

Voraussetzung ist eine aktuelle, installierte Java Version (https://foojay.io/almanac/java-17/).

Und eine heruntergeladene PDF-Version des Frisia Fahrplans von https://inselfaehre.de/juist, ganz unten auf der Seite unter Service "Download Fahplan Juist 202x"

### Windows
    .\gradlew.bat run --args="'C:\Eigene Dateien\Benutzer\fahrplan.pdf' 'C:\Eigene Dateien\Benutzer\neuerKalender.ics'"

*Natürlich ist der Pfad zur Datei anzupassen.*

### Linux
    ./gradlew run --args="/home/benutzer/Dokumente/fahrplan.pdf /home/benutzer/Dokumente/neuerKalender.ics"

*Natürlich ist der Pfad zur Datei anzupassen.*

## Gewähr
Das Projekt wurde *mal eben* programmiert, daher ist die Code-Qualität bescheiden und die Test nicht vorhanden. Es funktioniert mit dem Fahrplan 2023. Ob es die nächsten Jahre auch noch funktioniert, hängt davon ab, ob das PDF im gleichen Format zur Verfügung gestellt wird.

Es ist bewusst nur die Hinfahrt gewählt, weil man ja erst einmal auf die Insel muss. Wenn man da ist, lässt sich die Rückfahrt ja viel einfacher planen.

Sollte Frisia in der Zukunft eine eigene ics-Datei zur Verfügung stellen, sollte lieber selbige verwendet werden.

Die Fahrzeit ist mit 90 Minuten angegeben. Das ist natürlich keine garantierte Ankunftzeit. Auch die Abfahrtszeit ist ebenso wenig eine Garantie, sondern wie der Name eines Fahrplans suggeriert, nur ein Plan.
