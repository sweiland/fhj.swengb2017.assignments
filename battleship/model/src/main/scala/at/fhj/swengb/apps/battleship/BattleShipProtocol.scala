package at.fhj.swengb.apps.battleship

import at.fhj.swengb.apps.battleship.BattleShipProtobuf.BattleShipGame.{Orientation, Pos}
import at.fhj.swengb.apps.battleship.model._

import scala.collection.JavaConverters._

object BattleShipProtocol {

  def convert(g: BattleShipGame): BattleShipProtobuf.BattleShipGame = {
    BattleShipProtobuf.BattleShipGame.newBuilder()
      .setHeight(g.battleField.height)
      .setWidth(g.battleField.width)
      .addAllVessel(g.battleField.fleet.vessels.map(convert).asJava)
      .addAllAlreadyClicked(g.clickedPos.map(convert).asJava)
      .build()
  }

  private def convert(v: Vessel): BattleShipProtobuf.BattleShipGame.Vessel = {
    BattleShipProtobuf.BattleShipGame.Vessel.newBuilder()
      .setName(v.name.value)
      .setVesselSize(v.size)
      .setDirection(v.direction match {
        case Horizontal => Orientation.Horizontal
        case Vertical => Orientation.Vertical
      })
      .setPos(
        Pos.newBuilder()
          .setPosX(v.startPos.x.toString)
          .setPosY(v.startPos.y.toString)
          .build()
      )
      .build()
  }

  def convert(p: BattlePos): Pos = {
    Pos.newBuilder()
      .setPosX(p.x.toString)
      .setPosY(p.y.toString)
      .build()
  }

  def convert(g: BattleShipProtobuf.BattleShipGame): BattleShipGame = {
    val fleet = Fleet(g.getVesselList.asScala.map(convert).toSet)
    val battleField = BattleField(g.getWidth, g.getHeight, fleet)
    val alreadyClicked: List[BattlePos] = g.getAlreadyClickedList.asScala.map(convert).toList
    val game = BattleShipGame(battleField, e => e.toDouble, e => e.toDouble, e => println(e), e => println(e))
    game.clickedPos = alreadyClicked
    game
  }

  def convert(f: BattleShipProtobuf.BattleShipGame.Vessel): Vessel = {
    val name = NonEmptyString(f.getName)
    val orient: Direction = f.getDirection match {
      case Orientation.Horizontal => Horizontal
      case Orientation.Vertical => Vertical
    }
    Vessel(name, convert(f.getPos), orient, f.getVesselSize)
  }

  def convert(p: Pos): BattlePos = BattlePos(p.getPosX.toInt, p.getPosY.toInt)

}
