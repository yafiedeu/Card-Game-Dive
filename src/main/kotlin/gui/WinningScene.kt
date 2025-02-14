package gui

import service.RootService
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import java.awt.Color

/**
 * This class represents the winning scene that is displayed when the game ends.
 * It shows the scores of both players and the winner.
 * @param rootService The RootService instance holds the DiveGame state.
 */
class WinningScene(private val rootService: RootService) : MenuScene(400, 1080), Refreshable {
    private val headlineLabel = Label(
        width = 300, height = 50, posX = 50, posY = 50,
        text = "Game Over",
        font = Font(size = 22)
    )
    private val p1Label = Label(
        width = 300, height = 35, posX = 50, posY = 125,
        text = "1."
    )
    private val p2Label = Label(
        width = 300, height = 35, posX = 50, posY = 160,
        text = "2."
    )


    private val player1ResultLabel = Label(
        width = 300, height = 35, posX = 50, posY = 125, text = ""
    ).apply {  }

    private val player2ResultLabel = Label(
        width = 300, height = 35, posX = 50, posY = 160, text = ""
    ).apply {  }

    private val winnerLabel = Label(
        width = 300, height = 35, posX = 50, posY = 195, text = ""
    ).apply {  }


    private val gameResult = Label(width = 300, height = 35, posX = 50, posY = 195)

    val quitButton = Button(width = 140, height = 35, posX = 50, posY = 265, text = "Quit").apply {
        visual = ColorVisual(Color(221,136,136))

    }

    val startButton = Button(width = 140, height = 35, posX = 210, posY = 265, text = "New Game").apply {
        visual = ColorVisual(Color(136, 221, 136))
    }

    init {
        opacity = .5
        addComponents(
            headlineLabel,
            p1Label,
            player1ResultLabel,
            p2Label,
            player2ResultLabel,
            gameResult,
            startButton,
            quitButton,
            winnerLabel)
    }

    /**
     * This function updates the labels to display the scores of both players and the winner.
     * @throws IllegalStateException if no game is currently active.
     */
    fun gameEndsHeadlineText(){
        val game = rootService.currentGame
        checkNotNull(game) { "No game running" }
        player1ResultLabel.text = game.playerList[0].name + " scored " + game.playerList[0].score + " points"
        player2ResultLabel.text = game.playerList[1].name + " scored " + game.playerList[1].score + " points"
        winnerLabel.text = if(game.playerList[0].score > game.playerList[1].score)
            "${game.playerList[0].name} wins" else "${game.playerList[1].name} wins"

    }

}