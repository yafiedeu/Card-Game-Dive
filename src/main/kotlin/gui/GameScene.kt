package gui

import entity.Card
import entity.DiveGame
import service.CardImageLoader
import service.RootService
import tools.aqua.bgw.components.container.GameComponentContainer
import tools.aqua.bgw.components.container.LinearLayout
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.util.BidirectionalMap
import tools.aqua.bgw.visual.ColorVisual

/**
 * This class represents the game scene where the game is played.
 * It manages the UI and interaction of game components such as player hands, draw stack, discard stack, and action buttons.
 * @param rootService The RootService instance holds the DiveGame state.
 */
class GameScene(private val rootService: RootService) : BoardGameScene(1920, 1080), Refreshable {

    // Player 1 (bottom of screen)
    private val player1Label = Label(posX = 400, posY = 1000, text = "Player 1")
    private val player1CollectedStack = LabeledStackView(posX = 100, posY = 830, "CollectedStack")
    private val player1Hand = LinearLayout<CardView>(posX = 500, posY = 800, spacing = 50, width = 1000)


    // Player 2 (top of screen)
    private val player2Label = Label(posX = 400, posY = 100, text = "Player 2")
    private val player2CollectedStack = LabeledStackView(posX = 100, posY = 25, "collectedStack")
    private val player2Hand = LinearLayout<CardView>(posX = 500, posY = 100, spacing = 50, width = 2000)


    //Center of the screen
    private val discardStack = LabeledStackView(posX = 300, posY = 400, "discard stack")
    private val trioCardView = LinearLayout<CardView>(posX = 500, posY = 400, width = 400)

    private val drawStack = LabeledStackView(posX = 1100, posY = 400, "draw stack")

    private val drawButton = Button(posX = width - 250, posY = height - 300, text = "Draw").apply {
        onMouseClicked = {
            require(rootService.currentGame!!.currentPlayer.hand.size <= 9)
            { "You have to play a card or discard a card first" }
            disableActionButtons()
            rootService.playerActionService.drawCard()
            if (rootService.currentGame!!.currentPlayer.hand.size == 9) {
                playButton.isDisabled = false
                discardButton.isDisabled = false
            }
            endTurnButton.isDisabled = false

        }
    }

    private val discardButton: Button = Button(posX = width - 250, posY = height - 250, text = "Discard").apply {
        onMouseClicked = {
            rootService.currentGame?.let {
                val playerCard = cardMap.backward(chosenCard!!)
                rootService.playerActionService.discardCard(playerCard)
                disableActionButtons()
                endTurnButton.isDisabled = false

            }
        }
    }

    private val endTurnButton = Button(posX = width - 150, posY = height - 100, text = "End Turn").apply {
        onMouseClicked = {
            rootService.currentGame?.let {
                require (rootService.currentGame!!.currentPlayer.hand.size <= 8) {
                    "You must play a card or discard a card before ending your turn." }
                    rootService.gameService.endTurn()


            }
        }
    }


    /**
     * structure to hold pairs of (card, cardView) that can be used
     *
     * 1. to find the corresponding view for a card passed on by a refresh method (forward lookup)
     *
     * 2. to find the corresponding card to pass to a service method on the occurrence of
     * ui events on views (backward lookup).
     */
    private val cardMap: BidirectionalMap<Card, CardView> = BidirectionalMap()


    private var chosenCard: CardView? = null
    private var chosenTrioCard: CardView? = null
    private var chosenDiscardCard: CardView? = null


    private val playButton: Button = Button(posX = width - 400, posY = height - 300, text = "Play").apply {
        onMouseClicked = {
            val playerCard = cardMap.backward(chosenCard!!)
            rootService.playerActionService.playCard(playerCard)
            disableActionButtons()
            endTurnButton.isDisabled = false
        }
    }

    private val swapButton: Button = Button(posX = width - 400, posY = height - 250, text = "Swap").apply {
        onMouseClicked = {
            val game = rootService.currentGame
            checkNotNull(game) { "No started game found." }

            val playerCard = cardMap.backward(chosenCard!!)
            val trioCard = cardMap.backward(chosenTrioCard!!)
            rootService.playerActionService.swapCard(playerCard, trioCard)
            disableActionButtons()
            game.currentPlayer.swapped = true
            endTurnButton.isDisabled = false

        }
    }


    init {
        // dark green for "Casino table" flair
        background = ColorVisual(108, 168, 59)

        addComponents(
            player1Label,
            player1CollectedStack,
            player1Hand,
            player2Label,
            player2CollectedStack,
            player2Hand,
            discardStack,
            trioCardView,
            drawStack,
            endTurnButton,
            playButton,
            discardButton,
            swapButton,
            drawButton
        )
        enableActionButtons()
        endTurnButton.isDisabled = true

    }


    // Enables the action buttons (play, draw, swap)
    private fun enableActionButtons() {
        playButton.isDisabled = false
        drawButton.isDisabled = false
        swapButton.isDisabled = false
    }

    // Disables the action buttons (play, draw, swap).
    private fun disableActionButtons() {
        playButton.isDisabled = true
        drawButton.isDisabled = true
        swapButton.isDisabled = true
    }

    /**
     * Initializes the complete GUI for the game, i.e the cards in the players' hands, the draw stack, and the discard stack.
     */
    override fun refreshAfterStartNewGame() {
        val game = rootService.currentGame
        checkNotNull(game) { "No started game found." }

        cardMap.clear()

        val cardImageLoader = CardImageLoader()


        val otherPlayer = if (game.currentPlayer == game.playerList[0]) game.playerList[1] else game.playerList[0]
        initializeCardView(game.currentPlayer.hand, player1Hand, cardImageLoader)
        initializeCardView(otherPlayer.hand, player2Hand, cardImageLoader, false)

        for (cardView in player1Hand) {
            cardView.onMouseClicked = {
                chosenCard = cardView

            }
        }

        for (cardView in player2Hand) {
            cardView.onMouseClicked = {
                chosenCard = cardView

            }
        }

        initializeStackView(game.drawStack, drawStack, cardImageLoader)
        initializeStackView(game.discardStack, discardStack, cardImageLoader)

        initializeCardView(game.trio, trioCardView, cardImageLoader)


        updatePlayerLabels(game)

        player1CollectedStack.clear()
        player2CollectedStack.clear()

        enableActionButtons()
        endTurnButton.isDisabled = true


    }

    /**
     * Update [player1Label] and [player2Label] so that the player's name is shown.
     * @param game the current game
     */
    private fun updatePlayerLabels(game: DiveGame) {

        player1Label.text = game.currentPlayer.name
        player2Label.text =
            if (game.currentPlayer == game.playerList[0]) game.playerList[1].name else game.playerList[0].name
    }


    /**
     * Clears [stackView], adds a new [CardView] for each
     * element of [stack] onto it, and adds the newly created view/card pair
     * to the global [cardMap].
     * @param stack The list of cards to be displayed.
     * @param stackView The stack view to which the card views will be added.
     * @param cardImageLoader The loader to get the images for the card fronts and backs.
     */
    private fun initializeStackView(stack: List<Card>, stackView: LabeledStackView, cardImageLoader: CardImageLoader) {
        stackView.clear()
        stack.reversed().forEach { card ->
            val cardView = CardView(
                height = 200,
                width = 130,
                front = cardImageLoader.frontImageFor(card.cardsuit, card.cardValue),
                back = cardImageLoader.backImage
            )
            stackView.add(cardView)
            cardMap.add(card to cardView)
        }
    }

    /**
     * Initializes the card views for a list of cards.
     * This function clears the provided stack view and
     * creates a new `CardView` for each card in the given list.
     * It then adds each `CardView` to the stack view and updates the global `cardMap`
     * with the card and its corresponding view.
     * The cards can be optionally flipped to show either the front or back side.
     *
     * @param cardList The list of cards to be displayed.
     * @param stackView The stack view to which the card views will be added.
     * @param cardImageLoader The loader to get the images for the card fronts and backs.
     * @param flip A boolean indicating whether to show the front or the back side of the cards.
     */
    private fun initializeCardView(cardList: MutableList<Card>,
                                   stackView: LinearLayout<CardView>,
                                   cardImageLoader: CardImageLoader,
                                   flip: Boolean = true) {
        stackView.clear()
        cardList.forEach { card ->
            val cardView = CardView(
                height = 200,
                width = 130,
                front = cardImageLoader.frontImageFor(card.cardsuit, card.cardValue),
                back = cardImageLoader.backImage
            )
            if (flip)
                cardView.showCardSide(CardView.CardSide.FRONT)
            else
                cardView.showCardSide(CardView.CardSide.BACK)

            stackView.add(cardView)

            cardMap.add(card to cardView)
        }
    }


    override fun refreshAfterDrawCard(drawnCard: Card) {
        val game = rootService.currentGame
        checkNotNull(game) { "No started game found." }

        updatePlayerLabels(game)
        chosenCard = null

        reloadAll()
        playButton.isDisabled = false

        val drawnCardView = cardMap.forward(drawnCard)
        chosenCard = drawnCardView
        for (cardView in player1Hand) {
            cardView.onMouseClicked = {
                chosenDiscardCard = cardView


            }
        }
        for (cardView in player2Hand) {
            cardView.onMouseClicked = {
                chosenDiscardCard = cardView
            }
        }

        playButton.onMouseClicked = {
            val playerCard = cardMap.backward(chosenCard!!)
            rootService.playerActionService.playCard(playerCard)
            disableActionButtons()
            endTurnButton.isDisabled = false
            chosenCard = null
        }
        for (card in game.currentPlayer.hand) {
            val cardView = cardMap.forward(card)
            chosenDiscardCard = cardView
        }
        if (game.currentPlayer.hand.size == 9) {
            playButton.isDisabled = true
            drawButton.isDisabled = true
            swapButton.isDisabled = true
            discardButton.onMouseClicked = {
                val playerCard = cardMap.backward(chosenDiscardCard!!)
                rootService.playerActionService.discardCard(playerCard)
                disableActionButtons()
                endTurnButton.isDisabled = false
            }


        }



    }

    override fun refreshAfterPlayCard(playerCard: Card) {
        val game = rootService.currentGame
        checkNotNull(game) { "No started game found." }

        updatePlayerLabels(game)
        chosenCard = null
        reloadAll()
    }

    override fun refreshAfterDiscardCard(playerCard: Card) {
        val game = rootService.currentGame
        checkNotNull(game) { "No started game found." }

        updatePlayerLabels(game)
        chosenCard = null
        reloadAll()
    }

    override fun refreshAfterSwapCard(playerCard: Card, trioCard: Card) {
        val game = rootService.currentGame
        checkNotNull(game) { "No started game found." }

        updatePlayerLabels(game)
        chosenTrioCard = null
        chosenCard = null
        reloadAll()
    }

    override fun refreshAfterEndTurn() {

        val game = rootService.currentGame
        checkNotNull(game) { "No started game found." }

        updatePlayerLabels(game)
        chosenTrioCard = null
        chosenCard = null
        reloadAll()
    }

    /**
     * This function reloads the card views for a list of cards and adds them to the specified container.
     * This function clears the provided container and updates it with new [CardView] instances for each card in the list.
     * It ensures that the card views are correctly associated with the corresponding cards in the global [cardMap].
     * @param cards The list of cards to be displayed.
     * @param cardViews The container to which the card views will be added.
     * @param flip A boolean indicating whether to show the front side of the cards.
     */
    private fun reload(cards: MutableList<Card>, cardViews: GameComponentContainer<CardView>, flip: Boolean) {
        cardViews.clear()
        for (card in cards) {
            val cardView = cardMap.forward(card)
            if (cardView.parent != null) cardView.removeFromParent()
            if (flip)
                cardView.showCardSide(CardView.CardSide.FRONT)
            else
                cardView.showCardSide(CardView.CardSide.BACK)
            cardViews.add(cardView)
        }
    }

    /**
     * This function reloads all card views.
     * It reloads the card views for the current player's hand, the other player's hand,
     * the trio, the draw stack, the discard stack and the player stack of both players.
     * @throws IllegalStateException if no game is currently active.
     */
    private fun reloadAll() {
        val game = rootService.currentGame
        checkNotNull(game) { "No started game found." }

        val otherPlayer = if (game.currentPlayer == game.playerList[0]) game.playerList[1] else game.playerList[0]

        reload(game.currentPlayer.hand, player1Hand, true)
        reload(otherPlayer.hand, player2Hand, false)
        reload(game.trio, trioCardView, true)
        reload(game.drawStack, drawStack, false)
        reload(game.discardStack, discardStack, true)
        reload(game.currentPlayer.playerStack, player1CollectedStack, true)
        reload(otherPlayer.playerStack, player2CollectedStack, true)
    }

    override fun refreshAfterStartTurn() {
        val game = rootService.currentGame
        checkNotNull(game) { "No game is currently active." }

        updatePlayerLabels(game)
        enableActionButtons()
        endTurnButton.isDisabled = true
        for (cardView in trioCardView) {
            cardView.onMouseClicked = null
            cardView.onMouseClicked = {
                chosenTrioCard = cardView

            }
        }
        for (cardView in player1Hand) {
            cardView.onMouseClicked = {
                chosenCard = cardView
                chosenDiscardCard = cardView


            }
        }
        for (cardView in player2Hand) {
            cardView.onMouseClicked = {
                chosenCard = cardView
                chosenDiscardCard = cardView
            }
        }

    }

    override fun refreshAfterGameEnds() {
        val game = rootService.currentGame
        checkNotNull(game) { "No game is currently active." }

    }
}