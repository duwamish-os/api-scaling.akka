import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.testkit.javadsl.TestKit
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class ActorUnitSpecs {

  val actorSystem: ActorSystem = ActorSystem.create("event-system")

  @Test
  def test() = {

    new TestKit(actorSystem) {{

      val processor = Mockito.mock(classOf[Processor])
      Mockito.when(processor.process()).thenReturn("whats up bro")

      val actorConfig = Props.create(classOf[ArtistActor], processor)
      val actor = actorSystem.actorOf(actorConfig)

      val mockActor: TestKit = new TestKit(actorSystem)

      //actor.tell(mockActor.getRef, getRef)
      actor.tell("this is message from prayagupd", getRef)

      expectMsg(duration("1 second"), "whats up bro")


    }}

    println("xvsdvs")
  }

}

class Processor {
  def process(): String = "processed"
}

class ArtistActor(processor: Processor) extends Actor {

  override def receive = {
    case event: String =>
      println(s"ArtistActor ${sender()} sent me a message " + event)
      println("processro "+ processor)
      sender() ! processor.process()
  }

}

class SomeActor(processor: Processor) extends Actor {
  var target: ActorRef = null

  override def receive = {
    case x: String => processor.process()
  }

}
