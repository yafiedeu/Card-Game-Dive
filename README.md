# **Dive - Kartenspiel** 🃏🌊

**Dive** ist ein spannendes Kartenspiel für 2 Spieler, das in **Kotlin** entwickelt wurde und das **BoardGameWork**-Framework nutzt. Das Spiel fordert strategisches Denken und schnelle Entscheidungen, um als Erster alle Karten loszuwerden und die meisten Punkte zu sammeln. 💡

### **Spielmaterial** 🎴
- **52 Karten** im Standarddeck (Karo, Herz, Pik, Kreuz) × {2, 3, 4, 5, 6, 7, 8, 9, 10, Bube, Dame, König, Ass}
- Zu Beginn erhält jeder Spieler **5 Handkarten** ✋, der Rest kommt auf den Nachziehstapel.

### **Ziel des Spiels** 🎯
Das Ziel von **Dive** ist es, alle Karten aus der Hand und dem Nachziehstapel loszuwerden. Dabei sammelt man Punkte, indem man Triokarten ablegt:

- **5 Punkte** für Triokarten mit dem gleichen **Typ** ❤️♠️♦️♣️.
- **20 Punkte** für Triokarten mit dem gleichen **Wert**. 🔢

Der Spieler mit den meisten Punkten am Ende gewinnt! 🏆

### **Spielablauf** ⏳
Zu Beginn erhält jeder Spieler 5 Karten. Jeder Spieler kann maximal **8 Handkarten** halten. Die restlichen Karten werden im **Nachziehstapel** platziert. Der Spieler, der als Letztes eine Karte in die Mitte legt, darf die abgelegte **Triokarte** in seinen **Sammelstapel** legen. 🃏

#### **Aktionen** 🛠️
Während des Spiels können die Spieler folgende Aktionen durchführen:

- **Play**  🃏: Eine Karte aus der Hand in die Mitte legen. Sie muss entweder den gleichen **Typ** ❤️♠️♦️♣️ oder den gleichen **Wert** 🔢 wie die Karte in der Mitte haben.
- **Swap**  🔄: Eine Karte aus der Hand mit einer Karte aus der Mitte tauschen.
- **Draw**  🃎: Eine Karte vom Nachziehstapel ziehen und zur Hand hinzufügen.
- **Discard**  🗑️: Eine Karte abwerfen, wenn man mehr als 8 Handkarten besitzt.
