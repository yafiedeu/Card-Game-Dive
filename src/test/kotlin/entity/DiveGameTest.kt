package entity

import kotlin.test.*

/**
 * Test cases for the [DiveGame] class
 */
class DiveGameTest {

    /**
     * Test the creation of a DiveGame object.
     */
    @Test
    fun testDiveGameCreation() {
        val game = DiveGame()
        assertEquals(game.currentPlayer.name, "")
        assertTrue(game.playerList.isEmpty())
        assertTrue(game.discardStack.isEmpty())
        assertTrue(game.trio.isEmpty())
        assertTrue(game.drawStack.isEmpty())
    }


    /**
     * Test adding a player to the player list.
     */
    @Test
    fun testAddPlayer() {
        val game = DiveGame()
        val player = Player("Alice")
        game.playerList.add(player)
        assertEquals(1, game.playerList.size)
        assertEquals(player, game.playerList[0])
    }

    /**
     * Test setting the discard stack.
     */
    @Test
    fun testSetDiscardStack() {
        val game = DiveGame()
        val card = Card(CardSuit.SPADES, CardValue.ACE)
        game.discardStack.add(card)
        assertEquals(1, game.discardStack.size)
        assertEquals(card, game.discardStack[0])
    }

    /**
     * Test setting the draw stack.
     */
    @Test
    fun testSetDrawStack() {
        val game = DiveGame()
        val card = Card(CardSuit.SPADES, CardValue.ACE)
        game.drawStack.add(card)
        assertEquals(1, game.drawStack.size)
        assertEquals(card, game.drawStack[0])
    }
}