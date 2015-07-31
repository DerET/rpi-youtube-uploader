# RPi YouTube Uploader
Der RPi YouTube Uploader ist eine in Java geschrieben Software, die automatisch
alle Videos aus einem festgelegten Ordner hochlädt. Genutzt wird die [YouTube API v3]
(https://developers.google.com/youtube/v3/) und die maximale Upload-Geschwindigkeit
kann festgelegt werden. Auch wenn er in erster Linie zum Einsatz auf dem Raspberry Pi
geschrieben wurde lässt er sich auch auf jedem anderen System nutzen, das Java
unterstützt.

### JRE installieren
Zum Ausführen wird Java 7 oder höher benötigt. In Raspbian kann die Laufzeitumgebung
über die Paketquellen mithilfe einen der beiden folgenden Befehle installiert
werden:
```
apt-get install openjdk-7-jre
aptitude install openjdk-7-jre
```

### Selbst kompilieren (optional)
Zur Laufzeit wird eine Kopie der [Apache HttpComponents](https://hc.apache.org/)
und [Jackson](https://github.com/FasterXML/jackson) benötigt. Beide Projekte sind
unter den Bedingungen der [Apache License, Version 2.0]
(http://www.apache.org/licenses/LICENSE-2.0.txt) nutzbar. Die zur Entwicklung
genutzten Versionen können über [Apache Maven](https://maven.apache.org/) bezogen
werden:
```
org.apache.httpcomponents:httpclient:4.5
com.fasterxml.jackson.core:jackson-databind:2.5.4
```

### Fertiges Archiv entpacken
Für viele Revisionen ist ein tar-Archiv verfügbar, das eine aktuelle kompilierte
Kopie der Software enthält. Diese lässt sich mit folgendem Befehl in den meisten
Distributionen entpacken:
```
tar -xf ytuploader-*.tar
```
Anschließend kann das Archiv entfernt werden.

### Konfigurationsdatei anpassen
Die Datei settings.json enthält alle wichtigen Einstellungen und kann mit jedem
beliebigen Text-Editor bearbeitet werden.

`clientId` und `clientSecret` identifizieren die Anwendung gegenüber den Google-API-Servern.
Sollten diese ungültig werden kann ein eigenes Projekt in [Googles Developer Console]
(https://console.developers.google.com) erstellt und dessen Daten genutzt werden.

`path` gibt das Verzeichnis an, das auf hochzuladende Videos überwacht werden soll.
Trailing Slashes sind optional. Unter Windows können doppelte Backslashes für
Pfadangaben genutzt werden.

`extensions` ist ein Array zugelassener Dateitypen. Nur Dateien mit einer der
dort aufgeführten Dateiendungen werden hochgeladen.

`speedlimit` limitiert die zum Upload verwendete Bandbreite. Die angegebene Zahl
wird als Kilobyte pro Sekunde interpretiert, ist aber nur ein grober Richtwert
und durch interne Rundungen nicht völlig exakt. 0 bedeutet kein Limit.

### Ausführen
Bei der Installation über die Paketquellen wird Java in den Suchpfad für Programme
aufgenommen. Zum Ausführen genügt folgende Zeile:
```
java -jar ytuploader.jar
```
