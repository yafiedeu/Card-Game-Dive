package entity
/**
 * This class represents the Dive Game.
 *
 * [currentPlayer] is a list of players currently participating in the game.
 * This list represents the player currently in focus.
 *
 * [playerList] is a list of all players in the game.
 * This list can be modified to add or remove players as the game progresses.
 *
 * [discardStack] is the stack of cards that have been discarded during the game.
 *
 * [trio] Represents a trio of cards in the game.
 *
 * [drawStack] The stack of cards available for players to draw from.
 *
 * @throws IllegalStateException if there are no players in the game
 */
class DiveGame {
    var currentPlayer: Player = Player("")
    var playerList: MutableList<Player> = mutableListOf()
    var discardStack: MutableList<Card> = mutableListOf()
    var trio: MutableList<Card> = mutableListOf()
    var drawStack: MutableList<Card> = mutableListOf()
}
