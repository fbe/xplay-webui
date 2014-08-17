package actors

import java.net.InetSocketAddress
import java.nio.{ByteOrder, ByteBuffer}

import akka.actor.{ActorLogging, ActorRef, Actor}
import akka.event.LoggingReceive
import akka.io.{Udp, IO}
import akka.util.ByteString


class UDPDemoDataActor(remote: InetSocketAddress) extends Actor with ActorLogging {

  import context.system

  IO(Udp) ! Udp.SimpleSender

  override def receive = LoggingReceive {
    case Udp.SimpleSenderReady =>
      context.become(generatingData(sender()))
  }

  def generatingData(send: ActorRef): Receive = LoggingReceive {

    case "tick" =>

      val prefix = Array[Byte](68,65,84,65,60)
      val msgType = Array[Byte](20,0,0,0) // gps (position 20)

      val lat = 28.999f
      val lon = 10.835f
      val ftmsl = 30000f
      val ftagl = 28000f

      val byteBuffer = ByteBuffer.allocate(32)
      byteBuffer.order(ByteOrder.LITTLE_ENDIAN)
      byteBuffer.putFloat(lat)
      byteBuffer.putFloat(lon)
      byteBuffer.putFloat(ftmsl)
      byteBuffer.putFloat(ftagl)

      val payload = byteBuffer.array()

      val message = ByteBuffer.allocate(41)
      message.put(prefix)
      message.put(msgType)
      message.put(payload)

      println("sending")

      //send ! Udp.Send(ByteString(message), remote)
      println("msg: " + ByteString(message))
      send ! Udp.Send(ByteString("foobar"), remote)

    case x => println(x)
  }


}
