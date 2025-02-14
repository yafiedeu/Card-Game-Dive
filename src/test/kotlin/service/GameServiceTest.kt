package service

import kotlin.test.*

/**
 * The GameServiceTest class contains unit tests for the GameService class.
 * These tests ensure that the game logic of Dive Game is functioning correctly.
 */
class GameServiceTest {

    private val rootService = RootService()
    private val gameService = GameService(rootService)

    /**
     * Tests the startNewGame method of the GameService class.
     * Ensures that a new game is initialized correctly with two players, each having five cards,
     * and the draw stack containing the remaining 42 cards.
     */
    @Test
    fun startNewGame() {
        gameService.startNewGame("yafie", "burak")
        val game = rootService.currentGame
        checkNotNull(game) { "Game should be initialized" }

        assertEquals(2, game.playerList.size, "There should be 2 players")
        assertEquals(42, game.drawStack.size, "Draw stack should have 42 cards")
        assertEquals(5, game.playerList[0].hand.size, "Player 1 should have 5 cards in their hand")
        assertEquals(5, game.playerList[1].hand.size, "Player 2 should have 5 cards in their hand")
        assertNotEquals(game.playerList[0].hand, game.playerList[1].hand, "Players should have different cards")
        assertTrue(game.playerList.contains(game.currentPlayer), "Current player should be one of the players")
        assertEquals(rootService.currentGame?.drawStack?.size, 42, "draw stack should be size 42")
        assertFailsWith<IllegalArgumentException> {
            gameService.startNewGame("", "burak")
        }
        assertFailsWith<IllegalArgumentException> {
            gameService.startNewGame("yafie", "")
        }
        assertFailsWith<IllegalArgumentException> {
            gameService.startNewGame("yafie", "yafie")
        }
    }

    /**
     * Tests the startTurn method of the GameService class.
     * Ensures that the current player's turn is started correctly.
     */
    @Test
    fun testStartTurn() {
        gameService.startNewGame("yafie", "burak")
        gameService.startTurn()
        val game = rootService.currentGame
        checkNotNull(game) { "Game should be initialized" }

        val currentPlayer = game.currentPlayer
        assertNotNull(currentPlayer, "Current player should be set")
        println("It's ${currentPlayer.name}'s turn. They can choose to play a card, draw a card, or swap a card.")
    }

    /**
     * Tests the endTurn method of the GameService class.
     * Ensures that the turn is ended correctly and the current player is switched to the other player.
     */
    @Test
    fun testEndTurn() {
        gameService.startNewGame("yafie", "burak")
        val game = rootService.currentGame
        checkNotNull(game) { "Game should be initialized" }

        val initialPlayer = game.currentPlayer
        gameService.endTurn()
        val newPlayer = game.currentPlayer
        assertNotEquals(initialPlayer, newPlayer, "Current player should switch to the other player")
    }

}