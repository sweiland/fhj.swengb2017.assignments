package at.fhj.swengb.apps.battleship

import java.nio.file.{Files, Paths}

import at.fhj.swengb.apps.battleship.model._
import org.scalatest.WordSpecLike

class BattleShipProtocolSpec extends WordSpecLike {

  "BattleShipProtocol" should {
    "convert to protobuf" in {
      assert(Files.exists(Paths.get("./battleship.bin")))
    }
    "convert from protobuf correctly" in {
      val field = BattleField(10, 10, Fleet.Default)
      val game = BattleShipGame(field,
        e => e.toDouble,
        e => e.toDouble,
        e => println(e),
        e => println(e))
      val vessel = new BattleShip("Archduke John", BattlePos(0, 0), Vertical)
      val pos = BattlePos(1, 3)

      val actualGame = BattleShipProtocol.convert(game)
      val actualPos = BattleShipProtocol.convert(pos)
      val actualBattlePos = BattleShipProtocol.convert(actualPos)
      val actualVessel = BattleShipProtocol.convert(actualGame.getVessel(1))

      assert(field.height == actualGame.getHeight)
      assert(field.width == actualGame.getWidth)
      assert(pos.x.toString == actualPos.getPosX)
      assert(pos.y.toString == actualPos.getPosY)
      assert(actualBattlePos.x.toString == actualPos.getPosX)
      assert(actualBattlePos.y.toString == actualPos.getPosY)
      assert(actualVessel.name == vessel.name)
      assert(actualVessel.startPos == vessel.startPos)
      assert(actualVessel.direction == vessel.direction)
      assert(actualVessel.size == vessel.size)
    }
  }
}
