package service

import entity.*
import kotlin.random.Random

/**
 * The GameService class is responsible for managing the game logic of the Dive Game.
 * It handles the initialization of a new game, the start and end of turns, and the creation of the draw stack.
 *
 * @param rootService The RootService instance holds the DiveGame state.
 */
class GameService(private val rootService: RootService): AbstractRefreshingService() {
    /**
     * Starts a new game with the given player names.
     * Initializes the game state, assigns cards to players, and sets up the draw stack.
     *
     * @param player1Name The name of the first player.
     * @param player2Name The name of the second player.
     * @throws IllegalArgumentException if player names are empty or the same.
     */
    fun startNewGame(player1Name: String, player2Name: String) {

        require(player1Name.isNotBlank()) { "Player 1's name must not be empty or blank." }
        require(player2Name.isNotBlank()) { "Player 2's name must not be empty or blank." }
        require(player1Name != player2Name) { "Player names must not be the same." }

        val player1 = Player(player1Name)
        val player2 = Player(player2Name)

        // Initialize a new game
        rootService.currentGame = DiveGame()
        val game = rootService.currentGame
        checkNotNull(game) { "No game is currently active." }


        game.playerList.add(player1)
        game.playerList.add(player2)

        // Initialize and shuffle a 52-card standard deck
        val deck: MutableList<Card> = createDrawStack()

        // Assign the first five cards to player 1
        player1.hand.addAll(deck.subList(0, 5))

        // Assign the next five cards to player 2
        player2.hand.addAll(deck.subList(5, 10))

        // Set the remaining cards as the draw stack
        game.drawStack = deck.subList(10, deck.size).toMutableList()

        //Set the trio cards in the middle between 0 and 3 cards and the trio card is build from playCard function
        game.trio = mutableListOf()

        // Select a random player to start the game
        game.currentPlayer = if (Random.nextBoolean()) player1 else player2

        onAllRefreshables { refreshAfterStartNewGame() }

    }

    /**
     * Starts the turn for the current player.
     * The current player can choose to play a card, draw a card, or swap a card.
     * The actual choice will be handled in the GUI later.
     */
    fun startTurn() {
        val game = rootService.currentGame
        checkNotNull(game) { "No game is currently active." }

        println("It's ${game.currentPlayer.name}'s turn. They can choose to play a card, draw a card, or swap a card.")

        //Current player's hand card should not be hidden
        //game.currentPlayer.hand.forEach { it.isHidden = false }

        onAllRefreshables { refreshAfterStartTurn() }

    }

    /**
     * Ends the turn for the current player and switches to the other player.
     */
    fun endTurn() {
        val game = rootService.currentGame
        checkNotNull(game) { "No game is currently active." }

        // Switch to the other player
        game.currentPlayer = if (game.currentPlayer == game.playerList[0]) game.playerList[1] else game.playerList[0]
        onAllRefreshables { refreshAfterEndTurn() }

        // Check if the draw stack is empty and ends the game
        if (game.drawStack.isEmpty()) {
            onAllRefreshables { refreshAfterGameEnds() }
        }
    }

    /**
     * Creates a shuffled draw stack from the standard 52 deck of cards.
     */
    private fun createDrawStack(): MutableList<Card> {
        val deck = MutableList(52) { index ->
            Card(
                CardSuit.values()[index % CardSuit.values().size],
                CardValue.values()[index % CardValue.values().size]
            )
        }
        deck.shuffle()
        return deck
    }



}