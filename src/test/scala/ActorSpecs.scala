import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.testkit.{DefaultTimeout, TestKit, TestProbe}
import com.api.scaling.server.{EventsStatsActor, WordEvent, WordLengthEvent}
import org.scalatest._

import scala.language.postfixOps

class ActorSpecs extends TestKit(ActorSystem("api-scaling-aktorsystem")) with DefaultTimeout with FunSpecLike with Matchers with BeforeAndAfterAll {

  val statsActor: ActorRef = system.actorOf(Props[EventsStatsActor], name = "test")
  //val scalingTest: ActorRef = system.actorOf(Props[ScalingTestActor], "sfw")

  override protected def afterAll(): Unit = {
    system.stop(statsActor)
  }

  describe("my feature") {

    it("receives event") {

      val receiver = TestProbe()

      system.eventStream.subscribe(receiver.ref, classOf[WordLengthEvent])

      statsActor ! WordEvent("count my length")

      Thread.sleep(3000)

      receiver.expectMsg(WordLengthEvent("count my length", 15))
    }
  }

  class ScalingTestActor extends Actor {

    def receive: Receive = {

      case _ =>
        println("hi there")
        "hi there"
    }
  }
}
