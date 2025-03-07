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

✍️ Die Nummer hat das Format `N.m`, wobei `N` die Nummer der User Story ist, die der Testfall abdeckt, und `m` von `1` an nach oben gezählt. Beispiel: Der dritte Testfall, der die zweite User Story abdeckt, hat also die Nummer `2.3`.


## 2 Planen

| AP-№ | Frist | Zuständig | Beschreibung | geplante Zeit |
| ---- | ----- | --------- | ------------ | ------------- |
| 1.A  |       |           |              |               |
| ...  |       |           |              |               |

Total: 

✍️ Die Nummer hat das Format `N.m`, wobei `N` die Nummer der User Story ist, auf die sich das Arbeitspaket bezieht, und `m` von `A` an nach oben buchstabiert. Beispiel: Das dritte Arbeitspaket, das die zweite User Story betrifft, hat also die Nummer `2.C`.

✍️ Ein Arbeitspaket sollte etwa 45' für eine Person in Anspruch nehmen. Die totale Anzahl Arbeitspakete sollte etwa Folgendem entsprechen: `Anzahl R-Sitzungen` ╳ `Anzahl Gruppenmitglieder` ╳ `4`. Wenn Sie also zu dritt an einem Projekt arbeiten, für welches zwei R-Sitzungen geplant sind, sollten Sie auf `2` ╳ `3` ╳`4` = `24` Arbeitspakete kommen. Sollten Sie merken, dass Sie hier nicht genügend Arbeitspakte haben, denken Sie sich weitere "Kann"-User Stories für Kapitel 1.2 aus.

## 3 Entscheiden

Urpsrünglich war es im Projektantrag geplant, dass wir nicht nur ein Bingo für Items, sondern auch für Advancements und andere Aufgaben implementieren. Diese Idee haben wir jedoch recht schnell wieder verworfen, da wir es für unrealistisch hielten, dies auch noch in der geplanten Zeit umzusetzen.

## 4 Realisieren

| AP-№ | Datum | Zuständig | geplante Zeit | tatsächliche Zeit |
| ---- | ----- | --------- | ------------- | ----------------- |
| 1.A  |       |           |               |                   |
| ...  |       |           |               |                   |

✍️ Tragen Sie jedes Mal, wenn Sie ein Arbeitspaket abschließen, hier ein, wie lang Sie effektiv dafür hatten.

## 5 Kontrollieren

| TC-№ | Datum | Resultat | Tester |
| ---- | ----- | -------- | ------ |
| 1.1  |       |          |        |
| ...  |       |          |        |

✍️ Vergessen Sie nicht, ein Fazit hinzuzufügen, welches das Test-Ergebnis einordnet.

## 6 Auswerten

✍️ Fügen Sie hier eine Verknüpfung zu Ihrem Lern-Bericht ein.

