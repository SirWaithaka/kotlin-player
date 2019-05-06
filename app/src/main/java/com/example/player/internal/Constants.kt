package com.example.player.internal

import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

const val DEVICE_UID = 0
const val MINIMUM_STALE_THRESHOLD = 23
const val PLAY_DURATION_MILLIS: Long = 60 * 1000

val ZONE_ID: ZoneId = ZoneId.systemDefault()

val CURRENT_TIME: ZonedDateTime
   get () = ZonedDateTime.ofInstant(Instant.now(), ZONE_ID)

val START_OF_DAY: ZonedDateTime
   get() = CURRENT_TIME.toLocalDate().atStartOfDay(ZONE_ID)

val TIME_OF_DAY: String
   get() {
      return (CURRENT_TIME.hour - START_OF_DAY.hour).let {
         when  {
            it < 6 -> PLAYTIME.Q1.time
            it < 12 -> PLAYTIME.Q2.time
            it < 18 -> PLAYTIME.Q3.time
            it < 24 -> PLAYTIME.Q4.time
            else -> PLAYTIME.Q1.time
         }
      }
   }

enum class PLAYTIME(val time: String) {
   Q1("morning"),
   Q2("afternoon"),
   Q3("evening"),
   Q4("latenight")
}