package actors

import actors.UDPConnectionStatusActorMessages.Tick
import akka.actor.Props
import akka.routing._
import play.api.libs.concurrent.Akka._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by becker on 01.08.14.
 */


object ActorRegistry {

  import play.api.Play.current


  val resizerForPooledActors = DefaultResizer(lowerBound=2, upperBound=10)

  object Names {
    val udpConnectionStatusActor = "udpConnectionStatusActor"
    val gpsPositionActor = "gpsPositionActor"
    val pitchRollHeadingActor = "pitchRollHeadingActor"
    val speedActor = "speedActor"
    val xplaneReceiverActor = "xplaneReceiver"
    val websocketRegistryActor = "websocketRegistry"
  }

  val udpConnectionStatusActor = system.actorOf(Props[UDPConnectionStatusActor], name=Names.udpConnectionStatusActor)
  system.scheduler.schedule(0.milliseconds, 1.seconds, udpConnectionStatusActor, Tick)

  val gpsPositionActor = system.actorOf(Props[GPSPositionActor], name=Names.gpsPositionActor)
  val pitchRollHeadingActor = system.actorOf(Props[PitchRollHeadingActor], name=Names.pitchRollHeadingActor)


  val speedActor = system.actorOf(
    RoundRobinPool(
      nrOfInstances = 2,
      resizer = Some(resizerForPooledActors)
    ).props(Props[SpeedActor]), name="SpeedActorRouter")




  val xplaneDataReceiver = system.actorOf(Props[XPlaneUDPReceiverActor], name = Names.xplaneReceiverActor)
  val websocketRegistry = system.actorOf(Props[WebSocketRegistry], name = Names.websocketRegistryActor)


}
