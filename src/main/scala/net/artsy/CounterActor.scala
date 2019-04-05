package net.artsy

import akka.actor.{ Actor, Props }

object CounterActor {
  // actor messages
  sealed trait Message
  final case object Plus extends Message
  final case object Minus extends Message
  final case object Summary extends Message

  def props: Props = Props[CounterActor]
}

class CounterActor extends Actor {
  import CounterActor._
  var counter: Long = 0

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
  }
}
