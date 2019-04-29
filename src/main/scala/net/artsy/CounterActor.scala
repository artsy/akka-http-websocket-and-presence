package net.artsy

import akka.actor.{ Actor, ActorRef, Props }

object CounterActor {
  // actor messages
  sealed trait CounterActorMessage
  final case object Plus                          extends CounterActorMessage
  final case object Minus                         extends CounterActorMessage
  final case object Summary                       extends CounterActorMessage
  final case class UserJoined(actorRef: ActorRef) extends CounterActorMessage
  final case class UserLeft(actorRef: ActorRef)   extends CounterActorMessage

  def props: Props = Props[CounterActor]
}

class CounterActor extends Actor {
  import CounterActor._
  var counter: Long = 0

  // keep track of all websockets
  var participants = scala.collection.mutable.Set[ActorRef]()

  override def receive: Receive = {
    case Plus => {
      counter += 1
      sender() ! counter
    }

    case Minus => {
      counter -= 1
      sender() ! counter
    }

    case Summary => sender() ! counter

    case UserJoined(actorRef) => participants += actorRef

    case UserLeft(actorRef) => participants -= actorRef
  }
}
