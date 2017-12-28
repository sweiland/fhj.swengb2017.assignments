package at.fhj.swengb.apps.battleship.jfx

import java.net.URL
import java.nio.file.{Files, Paths}
import java.util.ResourceBundle
import javafx.fxml.{FXML, Initializable}
import javafx.scene.control.{Slider, TextArea}
import javafx.scene.layout.GridPane

import at.fhj.swengb.apps.battleship.BattleShipProtocol
import at.fhj.swengb.apps.battleship.model._


class BattleShipFxController extends Initializable {

  val clicked = List()
  var g: BattleShipGame = _
  @FXML private var battleGroundGridPane: GridPane = _
  /**
    * A text area box to place the history of the game
    */
  @FXML private var log: TextArea = _
  @FXML private var slider: Slider = _

  @FXML def newGame(): Unit = initGame()

  private def initGame(): Unit = {
    val game: BattleShipGame = createGame()
    init(game, clicked)
    appendLog("New game started.")
  }

  private def createGame(): BattleShipGame = {
    val field = BattleField(10, 10, Fleet(FleetConfig.Standard))

    val battleField: BattleField = BattleField.placeRandomly(field)

    BattleShipGame(battleField, getCellWidth, getCellHeight, upSlider, appendLog)
  }

  @FXML def slidermovement(): Unit = {
    val inFile = at.fhj.swengb.apps.battleship.BattleShipProtobuf.BattleShipGame.parseFrom(Files.newInputStream(Paths.get("./battleship.bin")))
    val loadGame: BattleShipGame = BattleShipProtocol.convert(inFile)
    val loadedGame = BattleShipGame(loadGame.battleField, getCellWidth, getCellHeight, upSlider, appendLog)
    val sliderVal: Int = slider.getValue.toInt
    val histPos: List[BattlePos] = loadGame.clickedPos.takeRight(sliderVal)
    loadedGame.simulateClicks(histPos)
    println(loadGame.clickedPos.takeRight(sliderVal))
  }

  @FXML def loadGame(): Unit = {
    val inFile = at.fhj.swengb.apps.battleship.BattleShipProtobuf.BattleShipGame.parseFrom(Files.newInputStream(Paths.get("./battleship.bin")))
    val loadGame: BattleShipGame = BattleShipProtocol.convert(inFile)
    val loadedGame = BattleShipGame(loadGame.battleField, getCellWidth, getCellHeight, upSlider, appendLog)
    val clicked = loadedGame.clickedPos
    init(loadedGame, clicked)
    loadedGame.simulateClicks(loadGame.clickedPos)
  }

  /**
    * Create a new game.
    *
    * This means
    *
    * - resetting all cells to 'empty' state
    * - placing your ships at random on the battleground
    *
    */
  def init(game: BattleShipGame, clickList: List[BattlePos]): Unit = {
    g = game
    battleGroundGridPane.getChildren.clear()
    for (c <- game.getCells()) {
      battleGroundGridPane.add(c, c.pos.x, c.pos.y)
    }
    game.getCells().foreach(c => c.init())
  }

  def appendLog(message: String): Unit = log.appendText(message + "\n")

  private def getCellHeight(y: Int): Double = battleGroundGridPane.getRowConstraints.get(y).getPrefHeight

  private def getCellWidth(x: Int): Double = battleGroundGridPane.getColumnConstraints.get(x).getPrefWidth

  def upSlider(max: Int): Unit = {
    slider.setMax(max)
    slider.setValue(max)
  }

  @FXML def saveGame(): Unit = {
    BattleShipProtocol.convert(g).writeTo(Files.newOutputStream(Paths.get("./battleship.bin")))
    println("Saved to" + Paths.get("./battleship.bin"))
  }

  override def initialize(url: URL, rb: ResourceBundle): Unit = initGame()
}