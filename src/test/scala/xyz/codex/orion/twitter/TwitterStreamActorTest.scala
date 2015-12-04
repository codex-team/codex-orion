package xyz.codex.orion.twitter

import akka.actor.{Actor, ActorSystem, Props}
import akka.io.IO
import akka.testkit.{ImplicitSender, TestActorRef, TestKit}
import org.specs2.mutable.SpecificationLike
import spray.can.Http
import spray.http._
import xyz.codex.orion.twitter.TwitterDSL.Tweet

import scala.io.Source


/**
  *
  * @author eliseev
  */
// TODO write anything!!! 
class TwitterStreamActorTest extends TestKit(ActorSystem())
  with SpecificationLike with ImplicitSender{
  sequential

  val port = 12345
  val tweetStream = TestActorRef(
    new TwitterStreamActor(Uri(s"http://localhost:$port/"), testActor) with TwitterAuthorization {
      def authorize = identity
    })

  "Streaming tweets" >> {

    "Should unmarshal one tweet" in {
      val twitterApi = TwitterApi(port)
      tweetStream ! "orion"  // our TwitterApi does not care

      val tweet = expectMsgType[Tweet]
      tweet.text mustEqual "Aggressive Ponytail #freebandnames"
      tweet.user.lang mustEqual "en"
      tweet.user.id mustEqual "137238150"
      tweet.place mustEqual None
      twitterApi.stop()
      success
    }
  }
}

class TwitterApi private(system: ActorSystem, port: Int, body: String) {

  private class Service extends Actor {

    def receive: Receive = {
      case _: Http.Connected =>
        sender ! Http.Register(self)
      case HttpRequest(HttpMethods.POST, _, _, _, _) =>
        sender ! ChunkedResponseStart(HttpResponse(StatusCodes.OK))
        sender ! MessageChunk(body = body)
        sender ! ChunkedMessageEnd()
    }
  }

  private val service = system.actorOf(Props(new Service))
  private val io = IO(Http)(system)
  io ! Http.Bind(service, "localhost", port = port)

  def stop(): Unit = {
    io ! Http.Unbind
    system.stop(service)
    system.stop(io)
  }
}

object TwitterApi {

  def apply(port: Int)(implicit system: ActorSystem): TwitterApi = {
    val body = Source.fromInputStream(getClass.getResourceAsStream("/tweet.json")).mkString
    new TwitterApi(system, port, body)
  }

}
