package helpers

import scala.concurrent.duration._

object TestParameters {

  val DEFAULT_RATE_REQ_PER_SECONDS = 1
  val DEFAULT_DURATION_IN_SECONDS = 1

  def getTestParameters: (Int, FiniteDuration) = {

    val rate = sys.env.get("RATE") match {
      case Some(value) => value.toInt
      case None => DEFAULT_RATE_REQ_PER_SECONDS
    }

    val duration = sys.env.get("DURATION") match {
      case Some(value) => value.toInt
      case None => DEFAULT_DURATION_IN_SECONDS
    }

    (rate, duration seconds)
  }

}
