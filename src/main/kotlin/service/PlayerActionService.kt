package service
import entity.*

/**
 * The PlayerActionService class is responsible for managing the player actions in the Dive Game.
 * It handles playing cards, drawing cards, swapping cards, and ending turns.
 *
 * @param rootService The RootService holds the DiveGame state.
 */
class PlayerActionService(private val rootService: RootService): AbstractRefreshingService() {

    /**
     * Plays a card from the player's hand and places it in the middle trio.
     * The card must have the same suit or value as the cards in the trio.
     * The player's score is updated based on the card's suit and value.
     *
     * @param playerCard The card to be played from the player's hand.
     * @throws IllegalStateException if no game is currently active or
     * if the player does not have the card in their hand.
     * @throws IllegalArgumentException if the card cannot be placed in the middle trio.
     */
    fun playCard(playerCard: Card) {
        val game = rootService.currentGame
        checkNotNull(game) { "No game is currently active." }

        require(game.currentPlayer.hand.contains(playerCard)) { "The player does not have this card in their hand." }

        val trio = game.trio

        // Check if the card can be placed in the middle
        if (trio.isEmpty() || trio.any { it.cardsuit == playerCard.cardsuit || it.cardValue == playerCard.cardValue }) {
            // Remove the card from the player's hand
            game.currentPlayer.hand.remove(playerCard)
            // Add the card to the trio in the middle
            trio.add(playerCard)
            // Update the score based on the trio
            if (trio.size == 3) {
                if (trio.all { it.cardsuit == playerCard.cardsuit }) {
                    game.currentPlayer.score += 5
                } else if (trio.all { it.cardValue == playerCard.cardValue }) {
                    game.currentPlayer.score += 20
                }
                //The trio will be emptied and the 3 trio card will be added to the playerstack
                game.currentPlayer.playerStack.addAll(trio)
                trio.clear()
                game.currentPlayer.swapped = false
                game.playerList[0].countSwap = 0
            }

        } else {
            throw IllegalArgumentException("The card cannot be placed in the middle. It must have the same suit or value as the cards in the trio.")
        }

        onAllRefreshables { refreshAfterPlayCard(playerCard) }

    }


    /**
     * Draws a card from the draw stack and adds it to the player's hand.
     * The player can optionally place the drawn card in the middle if it is a valid move.
     * The player's score is updated based on the card's suit and value.
     *
     * @throws IllegalStateException if no game is currently active or if the draw stack is empty.
     */
    fun drawCard() {
        val game = rootService.currentGame
        checkNotNull(game) { "No game is currently active." }

        val currentPlayer = game.currentPlayer
        val drawStack = game.drawStack
        require(drawStack.isNotEmpty()) { "The draw stack is empty." }

        // Draw a card from the draw stack
        val drawnCard = drawStack.removeFirst()
        currentPlayer.hand.add(drawnCard)

        onAllRefreshables { refreshAfterDrawCard(drawnCard) }

    }

    /**
     * Swaps a card from the player's hand with a card in the trio.
     * The new trio must still form a valid trio with the same suit or value.
     * The player's score is updated based on the card's suit and value.
     * If the player has more than 8 cards after the swap, they must discard one card.
     *
     * @param playerCard The card from the player's hand to be swapped.
     * @param trioCard The card from the trio to be swapped.
     * @throws IllegalStateException if no game is currently active.
     * @throws IllegalArgumentException if the player does not have the playerCard in their hand
     * or if the trio does not contain the trioCard.
     */
    fun swapCard(playerCard: Card, trioCard: Card) {
        val game = rootService.currentGame
        checkNotNull(game) { "No game is currently active." }
        //playerList[0] hat einmal geswapped und playerList[1] hat zweimal geswapped.
        require(game.currentPlayer.countSwap<2 ) {"The player can only swap twice in each round"}

        val currentPlayer = game.currentPlayer
        val trio = game.trio


        require(currentPlayer.hand.contains(playerCard)) { "The player does not have this card in their hand." }
        require(trio.contains(trioCard)) { "The trio does not contain the specified card." }

        // Remove the trioCard from the trio and add the playerCard to the trio
        trio.remove(trioCard)
        trio.add(playerCard)

        game.currentPlayer.countSwap++

        if(game.currentPlayer.countSwap == 2){
            game.currentPlayer.swapped = true
        }


        // Check if the new trio is valid
        if (trio.size <= 3 && (trio.all { it.cardsuit == playerCard.cardsuit } || trio.all { it.cardValue == playerCard.cardValue })) {
            // Remove the playerCard from the player's hand and add the trioCard to the player's hand
            currentPlayer.hand.remove(playerCard)
            currentPlayer.hand.add(trioCard)
        } else {
            throw IllegalArgumentException("The new trio is not valid. It must have the same suit or value.")
        }


        onAllRefreshables { refreshAfterSwapCard(playerCard, trioCard) }

    }

    /**
     * Discards a card from the player's hand to the discard stack.
     *
     * @param playerCard The card from the player's hand.
     * @throws IllegalStateException if no game is currently active.
     * @throws IllegalArgumentException if the player does not have the playerCard in their hand.
     */
    fun discardCard(playerCard: Card) {
        val game = rootService.currentGame
        checkNotNull(game) { "No game is currently active." }

        val currentPlayer = game.currentPlayer
        require(currentPlayer.hand.contains(playerCard)) { "The player does not have this card in their hand." }
        require(currentPlayer.hand.size > 8)
        { "You can only discard a card when you have more than 8 cards in hand." }

        // Remove the card from the player's hand and add it to the discard stack
        currentPlayer.hand.remove(playerCard)
        game.discardStack.add(playerCard)


        onAllRefreshables { refreshAfterDiscardCard(playerCard) }

    }
    /**
     * Function to check if the game has ended by checking if the draw stack is empty.
     * @return true if the draw stack is empty.
     * @throws IllegalStateException if no game is currently active.
     */
    private fun gameEnds():Boolean{
        val game = rootService.currentGame
        checkNotNull(game) { "No game is currently active." }

        return game.drawStack.isEmpty()

    }
}