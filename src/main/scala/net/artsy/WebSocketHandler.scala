package net.artsy

/**
 * Handle websockets message - in and out
 * to add:
 * - keep reference to all websockets so we can send out updates of the new count
 */
trait WebSocketHandler {

  // messages
  sealed trait InComingMessage

  final case object IncrementCount extends InComingMessage

  def handler = ???
}
