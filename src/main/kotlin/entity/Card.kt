package entity
/**
 * This class represents 52 different cards that used in the game.
 * @param cardsuit: represents 4 different types of card suits.
 * @param cardValue: represents 13 different types of card values.
 * [isHidden] represents whether the card is hidden or open
 */
class Card(val cardsuit: CardSuit,val cardValue: CardValue) {
    override fun toString() = "$cardsuit$cardValue"
    var isHidden: Boolean = false
}