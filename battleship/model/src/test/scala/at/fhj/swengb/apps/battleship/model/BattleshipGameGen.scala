package at.fhj.swengb.apps.battleship.model

import org.scalacheck.Gen


/**
  * Implement in the same manner like MazeGen from the lab, adapt it to requirements of BattleShip
  */
object BattleshipGameGen {

  val battleShipGameGen: Gen[BattleShipGame] = for {
    battlefield <- battleFieldGen
    clickedPos <- clickedPosGen
  } yield {
    val game = BattleShipGame(battlefield,
      (x => x.toDouble),
      (x => x.toDouble),
      (x => println(x)))
    game.clickedPos = clickedPos
    game
  }

  val maxWidth: Int = 10
  val maxHeight: Int = 10
  val fleetConfigSeq: Seq[FleetConfig] =
    Seq(FleetConfig.OneShip, FleetConfig.TwoShips, FleetConfig.Standard)

  val battleFieldGen: Gen[BattleField] = for {
    width <- Gen.chooseNum[Int](1, maxWidth)
    height <- Gen.chooseNum[Int](1, maxHeight)
    x <- Gen.chooseNum[Int](0, fleetConfigSeq.size - 1)
  } yield BattleField(width, height, Fleet(fleetConfigSeq(x)))

  val clickedPosGen: Gen[Seq[BattlePos]] = for {
    i <- Gen.chooseNum[Int](0, maxWidth * maxHeight)
    x <- Gen.chooseNum[Int](0, maxWidth - 1)
    y <- Gen.chooseNum[Int](0, maxHeight - 1)
  } yield {
    Seq.fill(i)(BattlePos(x, y))
  }

}
