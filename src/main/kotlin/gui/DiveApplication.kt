package gui

import service.RootService
import tools.aqua.bgw.core.BoardGameApplication

/**
 * Classes to implement the GUI of the Dive Game.
 */
class DiveApplication : BoardGameApplication("Dive Game"), Refreshable {

    // Central service from which all others are created/accessed
    // also holds the currently active game
    private val rootService = RootService()


    // This is where the actual game takes place
    private val gameScene = GameScene(rootService)

    // This menu scene is shown after application start and if the "start" button is clicked in the gameFinishedMenuScene
    private val mainMenuScene = MainMenuScene(rootService).apply {
        quitButton.onMouseClicked = {
            exit()
        }
    }

    // This menu scene is shown after the game is finished (i.e. no more cards to draw)
    private val winningScene = WinningScene(rootService).apply {
        startButton.onMouseClicked = {
            this@DiveApplication.showMenuScene(mainMenuScene)
        }
        quitButton.onMouseClicked = {
            exit()
        }
    }

    // This menu scene is shown after the current player has finished their turn and the next player has the turn.
    private val nextPlayerScene = NextPlayerScene(rootService)


    init {

        // all scenes and the application itself need to react to changes done in the service layer
        rootService.addRefreshables(
            this,
            gameScene,
            winningScene,
            mainMenuScene,
            nextPlayerScene
        )

        this.showMenuScene(mainMenuScene, 0)

    }

    override fun refreshAfterStartNewGame() {
        hideMenuScene()
        showGameScene(gameScene)
    }

    override fun refreshAfterEndTurn() {
        hideMenuScene()
        showMenuScene(nextPlayerScene)
        nextPlayerScene.turnStartHeadlineText()
    }

    override fun refreshAfterStartTurn() {
        hideMenuScene()
        showGameScene(gameScene)
    }

    override fun refreshAfterGameEnds() {
        hideMenuScene()
        showMenuScene(winningScene)
        winningScene.gameEndsHeadlineText()
    }

}

