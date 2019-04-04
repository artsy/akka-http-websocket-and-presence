package net.artsy

import akka.actor.{ Actor, Props }

object CounterActor {
  // actor messages
  final case object Plus
  final case object Minus
  final case object Summary

  def props: Props = Props[CounterActor]
}

class CounterActor extends Actor {
  import CounterActor._
  var counter: Long = 0

  override def receive: Receive = {
    case Plus => counter += 1
    case Minus => counter -= 1
    case Summary => sender() ! counter
  }
}

