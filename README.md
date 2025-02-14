Das Kartenspiel „Dive“ ist in Kotlin unter Verwendung des BoardGameWork programmiert.
„Dive“ ist ein Kartenspiel für 2 Spieler und wird mit einem Standard-Blatt bestehend aus 52 Karten gespielt:
{Karo, Herz, Pik, Kreuz} × {2, 3, 4, 5, 6, 7, 8, 9, 10, Bube, Dame, König, Ass}
Zu Beginn des Spiels erhält jeder Spieler 5 Handkarten und die restlichen Karten werden in den Nachziehstapel
Jeder Spieler darf maximal 8 Handkarte besitzen.
der Spieler der als Letztes seine Handkarte in die Mitte legt, bekommt die gelegte Triokarte in seinen Sammelstapel.
der Spieler bekommt 5 Punkte für die Triokarte mit dem gleichen Typ und 20 Punkte für die Triokarte mit dem gleichen Wert.
Das Ziel ist es, alle Karten aus dem Nachziehstapel und alle Handkarten loszuwerden, Ein Spieler gewinnt, wenn er mehr Punkte gesammelt hat.
folgende Aktionen können während des Spiels ausgeführt werden:
Play -> die ausgewählte Handkarte in die Mitte legen, sie kann nur in die Mitte gelegt werden, wenn die Karte die gleiche Farbe oder den gleichen Wert hat wie die Karte in der Mitte.
Swap -> eine ausgewählte Handkarte mit einer Karte aus der Mitte tauschen.
Draw -> eine Karte vom Nachziehstapel ziehen und in die Hand legen.
Discard -> eine ausgewälte Handkarte in den Abwurfstapel liegen, erst wenn man mehr als 8 Karten besitzt.
