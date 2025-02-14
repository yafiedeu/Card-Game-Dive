package entity

import kotlin.test.*

/**
 * Test cases for the [Card] class
 */
class CardTest {

    /**
     * Test the inequality of two Card objects.
     */
    @Test
    fun testCardInequality() {
        val card1 = Card(CardSuit.SPADES, CardValue.ACE)
        val card2 = Card(CardSuit.DIAMONDS, CardValue.ACE)
        assertNotEquals(card1, card2)
    }
}