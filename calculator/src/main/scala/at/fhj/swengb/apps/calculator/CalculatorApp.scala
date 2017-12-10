package at.fhj.swengb.apps.calculator

import java.net.URL
import java.util.ResourceBundle
import javafx.application.Application
import javafx.beans.property.{ObjectProperty, SimpleObjectProperty}
import javafx.event.ActionEvent
import javafx.fxml.{FXML, FXMLLoader, Initializable}
import javafx.scene.control.TextField
import javafx.scene.{Parent, Scene}
import javafx.stage.Stage

import scala.util.control.NonFatal
import scala.util.{Failure, Success}

object CalculatorApp {

  def main(args: Array[String]): Unit = {
    Application.launch(classOf[CalculatorFX], args: _*)
  }
}

class CalculatorFX extends javafx.application.Application {

  val fxml = "/at/fhj/swengb/apps/calculator/calculator.fxml"
  val css = "/at/fhj/swengb/apps/calculator/calculator.css"

  override def start(stage: Stage): Unit =
    try {
      stage.setTitle("Some crazy calculations are going on")
      setSkin(stage, fxml, css)
      stage.show()
      stage.setMinWidth(stage.getWidth)
      stage.setMinHeight(stage.getHeight)
    } catch {
      case NonFatal(e) => e.printStackTrace()
    }

  def setSkin(stage: Stage, fxml: String, css: String): Boolean = {
    val scene = new Scene(mkFxmlLoader(fxml).load[Parent]())
    stage.setScene(scene)
    stage.getScene.getStylesheets.clear()
    stage.getScene.getStylesheets.add(css)
  }

  def mkFxmlLoader(fxml: String): FXMLLoader = {
    new FXMLLoader(getClass.getResource(fxml))
  }

}

class CalculatorFxController extends Initializable {

  val calculatorProperty: ObjectProperty[RpnCalculator] = new SimpleObjectProperty[RpnCalculator](RpnCalculator())
  @FXML var num1: TextField = _
  @FXML var num2: TextField = _
  @FXML var num3: TextField = _
  var ca: Boolean = false

  override def initialize(location: URL, resources: ResourceBundle) = {

  }

  @FXML private def insertText(click: ActionEvent): Unit = {
    val button: String = click.getSource.asInstanceOf[javafx.scene.control.Button].toString
    button match {
      case "Button[id=bt1, styleClass=button]'1'" => insertThis("1")
      case "Button[id=bt2, styleClass=button]'2'" => insertThis("2")
      case "Button[id=bt3, styleClass=button]'3'" => insertThis("3")
      case "Button[id=bt4, styleClass=button]'4'" => insertThis("4")
      case "Button[id=bt5, styleClass=button]'5'" => insertThis("5")
      case "Button[id=bt6, styleClass=button]'6'" => insertThis("6")
      case "Button[id=bt7, styleClass=button]'7'" => insertThis("7")
      case "Button[id=bt8, styleClass=button]'8'" => insertThis("8")
      case "Button[id=bt9, styleClass=button]'9'" => insertThis("9")
      case "Button[id=bt0, styleClass=button]'0'" => insertThis("0")
      case "Button[id=enter, styleClass=button]'='" => onEnter
      case "Button[id=point, styleClass=button]'.'" => comma
      case "Button[id=plus, styleClass=button]'+'" => insertThis("Add")
      case "Button[id=minus, styleClass=button]'−'" => insertThis("Sub")
      case "Button[id=times, styleClass=button]'×'" => insertThis("Mul")
      case "Button[id=divided, styleClass=button]'÷'" => insertThis("Div")
      case "Button[id=clear, styleClass=button]'C'" => clear
      case "Button[id=plusminus, styleClass=button]'±'" => changeSign
      case _ => //Fallback – If this happens, some crazy shit is going on…
    }

    def insertThis(number: String): Unit = {
      number match {
        case "Add" => getCalculator().push(Op("+")) match {
          case Success(c) => setCalculator(c)
            num2.setText(getCalculator().stack.last match { case v: Val => v.value.toString })
            num3.setText("")
        }
        case "Sub" => getCalculator().push(Op("-")) match {
          case Success(c) => setCalculator(c)
            num2.setText(getCalculator().stack.last match { case v: Val => v.value.toString })
            num3.setText("")
        }
        case "Mul" => getCalculator().push(Op("*")) match {
          case Success(c) => setCalculator(c)
            num2.setText(getCalculator().stack.last match { case v: Val => v.value.toString })
            num3.setText("")
        }
        case "Div" => getCalculator().push(Op("/")) match {
          case Success(c) => setCalculator(c)
            num2.setText(getCalculator().stack.last match { case v: Val => v.value.toString })
            num3.setText("")
        }
        case _ =>
          if (num1.getText.isEmpty) num1.setText(number)
          else num1.setText(num1.getText + number)
      }
    }

    def comma: Unit = if (num1.getText.isEmpty) insertThis("0.") else if (num1.getText.count(_ == '.') < 1) insertThis(".")

    def onEnter: Unit = {
      if (!num1.getText.isEmpty) {
        sgn()
        if (true)
          if (num2.getText.isEmpty) {
            num2.setText(num1.getText)
            num1.setText("")
          }
          else {
            num3.setText(num2.getText)
            num2.setText(num1.getText)
            num1.setText("")
          }
      }
      else {
        insertThis("0")
      }
    }
  }

  def sgn(): Unit = {
    getCalculator().push(Op(num1.getText)) match {
      case Success(c) => setCalculator(c)
      case Failure(e) => e.getMessage
    }
    //getCalculator().stack foreach println
  }

  def setCalculator(rpnCalculator: RpnCalculator): Unit = calculatorProperty.set(rpnCalculator)

  def getCalculator(): RpnCalculator = calculatorProperty.get()

  def clear: Unit = {
    if (ca) {
      setCalculator(RpnCalculator())
      num1.setText("")
      num2.setText("")
      num3.setText("")
      ca = false
    }
    else {
      num1.setText("0")
      ca = true
    }
  }

  def changeSign: Unit = {
    if (!num1.getText.isEmpty) {
      if (num1.getText.head.equals('-'))
        num1.setText(num1.getText.tail)
      else num1.setText('-' + num1.getText)
    }
    else num1.setText("-")
  }

}
