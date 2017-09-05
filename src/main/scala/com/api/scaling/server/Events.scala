package com.api.scaling.server

final case class StatsEvent(payload: String)
final case class StatsResultNotification(meanWordLength: Double)
final case class EventFailed(reason: String)
