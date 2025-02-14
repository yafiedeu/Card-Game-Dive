package gui

import service.RootService
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual

/**
 * This class represents the Turn scene which pops up after the end turn of previous player.
 * The player who has turn now has to press start turn button to take the turn.
 * @param rootService The RootService instance holds the DiveGame state.
 */
class NextPlayerScene(private val rootService: RootService) : MenuScene(1920, 1080), Refreshable {

    private val headlineLabel = Label(
        width = 600, height = 150, posX = 1920/2 - 300, posY = 100,
        text = "",
        font = Font(size = 35)
    )

    private val startTurnButton = Button(
        width = 280, height = 70,
        posX = 1920/2- 150 , posY = 400,
        text = "Start Turn"
    ).apply {
        visual = ColorVisual(136, 221, 136)
        onMouseClicked = {
            rootService.gameService.startTurn()
        }
    }
    init {
        opacity = .5
        addComponents(
            headlineLabel,
            startTurnButton,
        )
    }

    /**
     * This function is used to display the name of the player who has the current turn.
     * @throws IllegalStateException if no game is currently active.
     */
    fun turnStartHeadlineText() {
        val game = rootService.currentGame
        checkNotNull(game) { "No started game found." }
        val str = game.currentPlayer.name + " has the turn."
        headlineLabel.text = str
    }

}