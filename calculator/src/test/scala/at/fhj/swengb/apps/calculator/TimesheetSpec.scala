package at.fhj.swengb.apps.calculator

import java.nio.file.{Files, Paths}

import org.scalatest.WordSpecLike

class TimesheetSpec extends WordSpecLike {
  "Timesheet" should {
    "Timesheet file exists" in {
      assert(Files.exists(Paths.get(
        "../calculator/timesheet-calculator.adoc")))
    }
  }
}
