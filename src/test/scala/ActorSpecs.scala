import java.util.concurrent.TimeUnit

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.pattern.ask
import akka.testkit.{DefaultTimeout, TestKit, TestProbe}
import akka.util.Timeout
import com.api.scaling.server.{EventsStatsActor, WordEvent, WordLengthEvent}
import org.scalatest._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.language.postfixOps

class ActorSpecs extends TestKit(ActorSystem("api-scaling-aktorsystem")) with DefaultTimeout with FunSpecLike with Matchers with BeforeAndAfterAll {

  val statsActor: ActorRef = system.actorOf(Props[EventsStatsActor], name = "test")
  //val scalingTest: ActorRef = system.actorOf(Props[ScalingTestActor], "sfw")
  implicit val timeoutt = Timeout(1, TimeUnit.SECONDS)

  override protected def afterAll(): Unit = {
    system.stop(statsActor)
  }

  describe("my feature") {

    it("receives event") {

      val receiver = TestProbe()

      system.eventStream.subscribe(receiver.ref, classOf[WordLengthEvent])

      statsActor ! WordEvent("count my length")

      receiver.expectMsg(WordLengthEvent("count my length", 15))
    }

    it("can ask") {

      val z: Future[Any] = talk

      z.map {
        case WordLengthEvent(a, b) => println("===>" + a)
        case _ => println("fjsdkl")
      }

      val result = Await.result(z, timeoutt.duration)

      result shouldBe WordLengthEvent("hi", 2)

    }

    it("can ask for future") {

      val z: Future[Any] = statsActor ? "process"

      z.map {
        case x: Int => println("===>" + x)
        case _ => println("fjsdkl")
      }

      val result = Await.result(z, timeoutt.duration)

      result shouldBe 100

    }

  }


  private def talk: Future[Any] = {
    val z = statsActor ? WordEvent("hi")
    z
  }

  class ScalingTestActor extends Actor {

    def receive: Receive = {

      case _ =>
        println("[INFO] ScalingTestActor hi there")
    }
  }

}
