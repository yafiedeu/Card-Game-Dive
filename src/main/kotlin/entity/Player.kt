package entity
/**
 * This class represents the players in the Dive Game.
 *
 * @param name is the name of the player in Dive Game.
 *
 * [score] The current score of the player. This starts at 0 and during the game
 * the score can be added plus 5 for the same card color and plus 20 for the same card value.
 *
 * [swapped] Indicates whether the player has swapped cards during their turn.
 * This is a boolean value that can be updated based on game actions.
 *
 * [hand] A mutable list of cards currently held by the player.
 * This list can be modified as the player draws or discards cards.
 *
 * [playerStack] A mutable list representing the cards that the player has in their stack.
 */
class Player(val name:String) {
    var score:Int = 0
    var swapped: Boolean = false
    var countSwap:Int =0
    var hand:MutableList<Card> = mutableListOf()
    var playerStack:MutableList<Card> = mutableListOf()
}