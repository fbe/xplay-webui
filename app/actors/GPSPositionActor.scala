package actors

import akka.actor.Actor
import model.{MessageBigDecimals, GPSPosition}
import utils.BigDecimalRounding

class GPSPositionActor() extends Actor with BigDecimalRounding with SaveLastMessage[GPSPosition] with StartStopLogging {

  receiver {
    case MessageBigDecimals(position) =>
      sendIfChanged(GPSPosition(position(0), position(1), r(position(2), 0), r(position(3), 0)))
  }
}



