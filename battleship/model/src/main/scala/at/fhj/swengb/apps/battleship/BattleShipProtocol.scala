package at.fhj.swengb.apps.battleship

import at.fhj.swengb.apps.battleship.BattleShipProtobuf.BattleShipGame.Orientation
import at.fhj.swengb.apps.battleship.model.{BattleShipGame, Horizontal, Vertical, Vessel}

import scala.collection.JavaConverters._

object BattleShipProtocol {

  def convert(g: BattleShipGame): BattleShipProtobuf.BattleShipGame = {
    BattleShipProtobuf.BattleShipGame.newBuilder()
      .setHeight(g.battleField.height)
      .setWidth(g.battleField.width)
      .addAllVessel(g.battleField.fleet.vessels.map(convert).asJava)
      .build()
  }

  def convert(v: Vessel): BattleShipProtobuf.BattleShipGame.Vessel = {
    BattleShipProtobuf.BattleShipGame.Vessel.newBuilder()
      .setName(v.name.value)
      .setVesselSize(v.size)
      .setDirection(v.direction match {
        case Horizontal => Orientation.Horizontal
        case Vertical => Orientation.Vertical
      })
      .setPos(
        BattleShipProtobuf.BattleShipGame.Pos.newBuilder()
          .setPosX(v.startPos.x)
          .setPosY(v.startPos.y)
          .build()
      )
      .build()
  }

  def convert(g: BattleShipProtobuf.BattleShipGame): BattleShipGame = ???

}
