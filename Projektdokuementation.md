# Projekt-Dokumentation

## 1 Informieren

### 1.1 Unser Projekt

In unserem Projekt möchten wir ein Minecraft Plugin erstellen, mit dem es möglich sein soll, innerhalb von Minecraft Bingo zu spielen. Einzelne Punkte auf der Bingo Karte sollen erledigt werden, indem die Spieler bestimmte Items finden.

### 1.2 User Stories

| US-№ | Verbindlichkeit | Typ  | Beschreibung                       |
| ---- | --------------- | ---- | ---------------------------------- |
| 1    |   Muss              | Funktional     | Als Spieler möchte ich, dass es einen Timer gibt, damit ich die schon vergangene Zeit sehen kann. |
| 2  |    Muss             |  Funktional    |   Als Spieler möchte ich, dass ich in einem Team sein kann, um gemeinsam Items zu sammeln.           |
| 3  |    Muss             |  Funktional    |    Als Spieler möchte ich, dass ich die Bingo Items anzeigen lassen kann, damit ich weiss, was ich noch alles sammeln muss.   |
| 4  |    Muss             |  Funktional    |    Als Spieler möchte ich, dass erkannt wird, wenn ich ein Item von der Bingokarte einsammle, damit es auf der Bingokarte abgehakt werden kann. |
| 5  |    Muss             |  Funktional    |    Als Spieler möchte ich, dass angezeigt wird, wenn ein Team eine Reihe von Items gefunden hat, damit ich weiss wer gewonnen hat.  |
| 6  |    Muss             |  Funktional    |    Als Spieler möchte ich, dass ich das Spiel über Commands kontrolliert werden kann, damit ich das Spiel konfigurieren kann.   |
| 7  |    Muss             |  Funktional    |    Als Spieler möchte ich, dass ich die Welt zurücksetzen kann, um auf einer neuen Welt eine neue Runde Bingo zu spielen.   |
| 8  |    Muss             |  Funktional    |    Als Server-Admin möchte ich, dass ich die Adresse des externen Services konfigurieren kann, damit ich diesen auch selber hosten könnte.   |
| 9  |    Muss             |  Rand    |  Als ein Developer möchte ich, dass das Plugin in zwei aufgeteilt wird, damit der Teil der auf dem Minecraft Server läuft, performanter ist.     |
| 10 |    Muss             |  Rand    |  Als ein Developer möchte ich, dass das Minecraft Plugin in Java und der externe Service in C# umgesetzt wird.     |


### 1.3 Testfälle

| TC-№ | Ausgangslage | Eingabe | Erwartete Ausgabe |
| ---- | ------------ | ------- | ----------------- |
| 1.1  |  Plugin aktiviert  |  /bingo start |  "START", Spieler erhält Liste mit Items, Timer beginnt zu zählen    |
| 1.2  |  Plugin aktiviert, Spiel läuft bereits  | /bingo pause | "Timer ist pausiert"  |
| 2.1  |  Plugin aktiviert | /bingo team join 1  | "Du bis nun in Team 1"  |
| 2.2  |  Plugin aktiviert, Spieler befindet sich in Team 1 | /bingo team leave 1  | "Du bist nicht mehr in Team 1"  |
| 3.1  |  Plugin aktiviert, /bingo start | /bingo | "Bingo Items: *Liste mit Items*"  |
| 3.2  |  Plugin aktiviert | /bingo start  | *Spieler erhält eine Liste mit allen Items*  |
| 4.1  |  Plugin aktiviert,/bingo start, *Eisentür ist auf der Bingokarte* | Spieler sammelt Eisentür ein |"*Spielername* hat das Item Eisentür gefunden", Eisentür wird von der Karte abgehakt  |
| 5.1  |  Plugin aktiviert, /bingo start, Team 1 findet 4 Items in einer Reihe auf der Karte |  Team 1 sammelt 5. Item aus der Reihe ein  |  "Team 1 hat das Spiel gewonnen!"  |
| 6.1  |  Plugin aktiviert, /bingo start |  /bingo toggle  |  "Die Timerrichtung wurde umgedreht", *Timer läuft nun rückwärts*  |
| 6.2  |  Plugin aktiviert, /bingo start |  /bingo set 1000  |  "Der Timer wurde auf 600 gesetzt", "00:10:00"  |
| 6.3  |  Plugin aktiviert, /bingo start, /bingo stop |  /bingo resume  |  "Der Timer wurde fortgesetzt"  |
| 6.4  |  Plugin aktiviert |  /bingo help  |  "Verfügbare Commands: /bingo <start|stop|resume|set|team|url|reset>"  |
| 7.1  |  Plugin aktiviert |  /bingo reset  |  "Die Welt wurde zurückgesetzt"  |
| 8.1  |  Plugin aktiviert |  /bingo url https://www.meinlustigerserver.ch/bingo  |  "Die Serveradresse wurde auf "https://www.meinlustigerserver.ch/bingo" gesetzt.  |



## 2 Planen

| AP-№ | Frist | Zuständig | Beschreibung | geplante Zeit (min) |
| ---- | ----- | --------- | ------------ | ------------- |
| 0.A  | 07.03.25 | Manuel Greub | Plugin aufsetzen | 30 |
| 1.A  | 07.03.25 | Manuel Greub | Es gibt einen Timer der hochzählt | 60 |
| 1.B  | 07.03.25 | Manuel Greub | Der Timer ist standardmässig pausiert | 10 |
| 1.C  | 07.03.25 | Manuel Greub | Der Timer kann mit /bingo start gestartet werden | 10 |
| 2.A  | 07.03.25 | Manuel Greub | Es gibt verschiedene Teams, die eine Liste von Spielern beinhalten | 100 |
| 2.B  | 07.03.25 | Manuel Greub | Teams können über /bingo team verwaltet werden | 40 |
| 2.C  | 07.03.25 | Manuel Greub | Wenn ein Team ein Item einsammelt, wird es für das ganze Team erledigt | 50 |
| 3.A  | 07.03.25 | Manuel Greub | Der Spieler kann mit dem Befehl /bingo eine Übersicht mit allen Items anzeigen lassen | 120 |
| 4.A  | 07.03.25 | Manuel Greub | Das Plugin nimmt eine Liste mit Items vom Server entgegen. | 180 |
| 4.B  | 07.03.25 | Manuel Greub | Wenn ein Spieler ein Item einsammelt, wird die aktualisierte Liste an den Server gesendet | 60 |
| 4.C  | 07.03.25 | Manuel Greub | Das Bild mit allen Items wird Ingame angezeigt  | 50 |
| 4.D  | 07.03.25 | Manuel Greub | Wenn ein Spieler ein Item einsammelt, wird die aktualisierte Karte allen Spielern angezeigt | 120 |
| 5.A  | 07.03.25 | Manuel Greub | Das Plugin zeigt an, wenn ein Team alles gefunden hat und zeigt den Gewinner an. | 60 |
| 6.A  | 07.03.25 | Manuel Greub | Implementierung von /bingo toggle | 40 |
| 6.B  | 07.03.25 | Manuel Greub | Implementierung von /bingo set | 20 |
| 6.C  | 07.03.25 | Manuel Greub | Implementierung von /bingo help | 20 |
| 7.A  | 07.03.25 | Manuel Greub | Welt kann zurückgesetzt werden | 60 |
| 7.B  | 07.03.25 | Manuel Greub | Die Welt kann über /bingo reset zurückgesetzt werden | 20 |
| 8.A  | 07.03.25 | Manuel Greub | Die Backend Adresse kann über /bingo url verändert werden | 60 |

Total: 


## 3 Entscheiden

Ursprünglich war es im Projektantrag geplant, dass wir nicht nur ein Bingo für Items, sondern auch für Advancements und andere Aufgaben implementieren. Diese Idee haben wir jedoch recht schnell wieder verworfen, da wir es für unrealistisch hielten, dies auch noch in der geplanten Zeit umzusetzen.

## 4 Realisieren

| AP-№ | Datum | Zuständig | geplante Zeit | tatsächliche Zeit |
| ---- | ----- | --------- | ------------- | ----------------- |
| 1.A  |       |           |               |                   |
| ...  |       |           |               |                   |



## 5 Kontrollieren

| TC-№ | Datum   | Resultat | Tester |
| ---- | ------- | -------- | ------ |
| 1.1  | 07.03.25 | OK       | Greub  |
| 1.2  | 07.03.25 | OK       | Greub  |
| 2.1  | 07.03.25 | OK       | Greub  |
| 2.2  | 07.03.25 | OK       | Greub  |
| 3.1  | 07.03.25 | OK       | Greub  |
| 3.2  | 07.03.25 | OK       | Greub  |
| 4.1  | 07.03.25 | OK       | Greub  |
| 5.1  | 07.03.25 | OK       | Greub  |
| 6.1  | 07.03.25 | OK       | Greub  |
| 6.2  | 07.03.25 | OK       | Greub  |
| 6.3  | 07.03.25 | OK       | Greub  |
| 6.4  | 07.03.25 | OK       | Greub  |
| 7.1  | 07.03.25 | OK       | Greub  |
| 8.1  | 07.03.25 | OK       | Greub  |

Alle Testfälle konnten ohne Probleme durchgeführt werden.

## 6 Auswerten

[Portfolio von Manuel Greub](https://portfolio.bbbaden.ch/view/view.php?t=cbd9d233fbd20ef6c22a)

