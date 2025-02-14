package gui

import entity.*

/**
 * This interface provides a mechanism for the service layer classes to communicate
 * (usually to the view classes) that certain changes have been made to the entity
 * layer, so that the user interface can be updated accordingly.
 *
 * Default (empty) implementations are provided for all methods, so that implementing
 * UI classes only need to react to events relevant to them.
 *
 */
interface Refreshable {

    /**
     * perform refreshes that are necessary after a player has played a card
     * @param playerCard the card that the player played
     */
    fun refreshAfterPlayCard(playerCard: Card){}

    /**
     * perform refreshes that are necessary after a player has drawn a card
     * @param drawnCard the card that the player drew
     */
    fun refreshAfterDrawCard(drawnCard: Card){}

    /**
     * perform refreshes that are necessary after a player has swapped a card
     * @param playerCard the card that the player swapped
     * @param trioCard the card in the middle that the player swapped with
     */
    fun refreshAfterSwapCard(playerCard: Card, trioCard: Card){}

    /**
     * perform refreshes that are necessary after a player has discarded a card
     * @param playerCard the card that the player discarded
     */
    fun refreshAfterDiscardCard(playerCard: Card){}

    /**
     * perform refreshes that are necessary after a new game started
     */
    fun refreshAfterStartNewGame(){}

    /**
     * perform refreshes that are necessary after a player has started their turn.
     */
    fun refreshAfterStartTurn(){}

    /**
     * perform refreshes that are necessary after a player has ended their turn
     */
    fun refreshAfterEndTurn(){}

    /**
     * perform refreshes that are necessary after the game has ended.
     */
    fun refreshAfterGameEnds(){}
}