package entity

import kotlin.test.*

/**
 * Test cases for the [Player] class
 */
class PlayerTest {

    /**
     * Test the creation of a Player object.
     */
    @Test
    fun testPlayerCreation() {
        val player = Player("Alice")
        assertEquals("Alice", player.name)
        assertEquals(0, player.score)
        assertFalse(player.swapped)
        assertTrue(player.hand.isEmpty())
        assertTrue(player.playerStack.isEmpty())
    }

    /**
     * Test updating the player's score.
     */
    @Test
    fun testUpdateScore() {
        val player = Player("Alice")
        player.score += 5
        assertEquals(5, player.score)
        player.score += 20
        assertEquals(25, player.score)
    }

    /**
     * Test updating the player's swapped status.
     */
    @Test
    fun testUpdateSwapped() {
        val player = Player("Alice")
        player.swapped = true
        assertTrue(player.swapped)
    }

    /**
     * Test adding cards to the player's hand.
     */
    @Test
    fun testAddToHand() {
        val player = Player("Alice")
        val card = Card(CardSuit.SPADES, CardValue.ACE)
        player.hand.add(card)
        assertEquals(1, player.hand.size)
        assertEquals(card, player.hand[0])
    }

    /**
     * Test adding cards to the player's stack.
     */
    @Test
    fun testAddToPlayerStack() {
        val player = Player("Alice")
        val card = Card(CardSuit.SPADES, CardValue.ACE)
        player.playerStack.add(card)
        assertEquals(1, player.playerStack.size)
        assertEquals(card, player.playerStack[0])
    }
}