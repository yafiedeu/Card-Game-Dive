package service

import kotlin.test.*
import entity.Card
import entity.CardSuit
import entity.CardValue

/**
 * The PlayerActionServiceTest class contains tests for the PlayerActionService class.
 * These tests ensure that the player actions in the Dive Game are functioning correctly.
 */
class PlayerActionServiceTest {

    private val rootService = RootService()
    private val gameService = GameService(rootService)
    private val playerActionService = PlayerActionService(rootService)

    // Cards for testing
    private val jackOfClubs = Card(CardSuit.CLUBS, CardValue.JACK)
    private val jackOfHearts = Card(CardSuit.HEARTS, CardValue.JACK)
    private val jackOfDiamonds = Card(CardSuit.DIAMONDS, CardValue.JACK)
    private val aceOfSpades = Card(CardSuit.SPADES, CardValue.ACE)
    private val fiveOfSpades = Card(CardSuit.SPADES, CardValue.FIVE)
    private val sevenOfSpades = Card(CardSuit.SPADES, CardValue.SEVEN)
    private val queenOfHearts = Card(CardSuit.HEARTS, CardValue.QUEEN)

    @BeforeTest
    fun setup() {
        gameService.startNewGame("Yafie", "Burak")
    }


    /**
     * Tests the playCard method of the PlayerActionService class.
     * Ensures that a card is correctly played from the player's hand to the trio in the middle.
     */
    @Test
    fun testPlayCard() {
        val game = rootService.currentGame
        checkNotNull(game) { "Game should be initialized" }

        val currentPlayer = game.currentPlayer
        val cardToPlay = currentPlayer.hand.first()

        playerActionService.playCard(cardToPlay)

        assertFalse(currentPlayer.hand.contains(cardToPlay), "cardToPlay should be removed from the player's hand")
        assertTrue(game.trio.contains(cardToPlay), "cardToPlay should be added to the trio in the middle")

        // Test playing a card to form a trio of the same value
        game.trio.removeAll(game.trio)
        game.trio.add(jackOfClubs)
        game.trio.add(jackOfHearts)
        currentPlayer.hand.add(jackOfDiamonds)

        playerActionService.playCard(jackOfDiamonds)

        assertEquals(20, currentPlayer.score, "Player should earn 20 points for a trio of the same value")

        // Test playing a card to form a trio of the same suit
        currentPlayer.score = 0
        game.trio.removeAll(game.trio)
        game.trio.add(aceOfSpades)
        game.trio.add(fiveOfSpades)
        currentPlayer.hand.add(sevenOfSpades)

        playerActionService.playCard(sevenOfSpades)

        assertEquals(5, currentPlayer.score, "Player should earn 5 points for a trio of the same suit")

        //Test playing an invalid card (card does not match the current trio)
        // Add a card to the trio that does not match the card to play
        game.trio.add(queenOfHearts)

        assertFailsWith<IllegalArgumentException> {
            playerActionService.playCard(cardToPlay)
        }

    }

    /**
     * Tests the drawCard method of the PlayerActionService class.
     * Ensures that a card is correctly drawn from the draw stack and added to the player's hand.
     */
    @Test
    fun testDrawCard() {
        val game = rootService.currentGame
        checkNotNull(game) { "Game should be initialized" }

        val initialDrawStackSize = game.drawStack.size
        playerActionService.drawCard()

        assertEquals(initialDrawStackSize - 1, game.drawStack.size, "The draw stack size should decrease by 1")
        //Tests drawing a card when the draw stack is empty.
        // Empty the draw stack
        game.drawStack.removeAll(game.drawStack)

        assertFailsWith<IllegalArgumentException> {
            playerActionService.drawCard()
        }

    }

    /**
     * Tests the swap method of the PlayerActionService class.
     * Ensures that a card is correctly swapped between the player's hand and the trio in the middle.
     */
    @Test
    fun testSwap() {
        val game = rootService.currentGame
        checkNotNull(game) { "Game should be initialized" }

        val currentPlayer = game.currentPlayer
        val playerCard = currentPlayer.hand.first()

        // Ensure the trio has at least one card to swap with
        if (game.trio.isEmpty()) {
            game.trio.add(game.drawStack.removeAt(0))
        }

        val trioCard = game.trio.first()

        playerActionService.swapCard(playerCard, trioCard)

        assertTrue(currentPlayer.hand.contains(trioCard), "The player's hand should contain the swapped trio card")
        assertFalse(currentPlayer.hand.contains(playerCard), "The player's hand should not contain the swapped player card")
        assertTrue(game.trio.contains(playerCard), "The trio should contain the swapped player card")
        assertFalse(game.trio.contains(trioCard), "The trio should not contain the swapped trio card")
    }

    /**
     * Tests the discardCard method of the PlayerActionService class.
     * Ensures that a card is correctly discarded from the player's hand to the discard stack.
     */
    @Test
    fun testDiscardCard() {
        val game = rootService.currentGame
        checkNotNull(game) { "Game should be initialized" }

        val currentPlayer = game.currentPlayer
        val cardToDiscard = currentPlayer.hand.first()

        playerActionService.discardCard(cardToDiscard)

        assertFalse(currentPlayer.hand.contains(cardToDiscard), "The card should be removed from the player's hand")
        assertTrue(game.discardStack.contains(cardToDiscard), "The card should be added to the discard stack")

        //Test discarding a card when the player has more than 8 cards
        currentPlayer.hand.add(jackOfClubs)
        currentPlayer.hand.add(jackOfHearts)
        currentPlayer.hand.add(jackOfDiamonds)
        currentPlayer.hand.add(queenOfHearts)
        currentPlayer.hand.add(aceOfSpades)
        // Ensure the player has more than 8 cards
        assertTrue(currentPlayer.hand.size > 8, "The player should have more than 8 cards")

        game.discardStack.removeAll(game.discardStack)

        playerActionService.discardCard(currentPlayer.hand.first())

        // Verify that a card has been discarded
        assertEquals(8, currentPlayer.hand.size, "The player's hand should be reduced to 8 cards")
        assertEquals(1, game.discardStack.size, "One card should be added to the discard stack")

        //Tests discarding a card when the hand is empty
        // Empty the player's hand
        currentPlayer.hand.removeAll(currentPlayer.hand)

        assertFailsWith<IllegalArgumentException> {
            playerActionService.discardCard(cardToDiscard)
        }
    }
}