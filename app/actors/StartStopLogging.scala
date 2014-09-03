package actors

import akka.actor.Actor
import play.api.Logger

trait StartStopLogging extends Actor {

  override def preStart() {
    Logger.info(s"Starting ${self.path}")
    super.preStart()
  }

  override def postStop() = {
    Logger.info(s"Stopping ${self.path}")
    super.postStop()
  }

}
