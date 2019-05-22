package com.example.player.internal

import android.os.Environment
import android.util.SparseIntArray
import android.view.Surface
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

const val DEVICE_UID = 0
const val MINIMUM_STALE_THRESHOLD = 23
const val PLAY_DURATION_MILLIS: Long = 60 * 1000

const val IMAGE_HEIGHT: Int = 1080
const val IMAGE_WIDTH: Int = 1920
const val CHANNEL_ID = "imageCaptureService"
const val IMAGE_CAPTURE_SERVICE_NAME = "Image Capture Service"

val ZONE_ID: ZoneId = ZoneId.systemDefault()

val CURRENT_TIME: ZonedDateTime
   get () = ZonedDateTime.ofInstant(Instant.now(), ZONE_ID)

val START_OF_DAY: ZonedDateTime
   get() = CURRENT_TIME.toLocalDate().atStartOfDay(ZONE_ID)

val TIME_OF_DAY: String
   get() {
      return (CURRENT_TIME.hour - START_OF_DAY.hour).let {
         when  {
            it < 6 -> PLAYTIME.Q4.time
            it < 12 -> PLAYTIME.Q1.time
            it < 18 -> PLAYTIME.Q2.time
            it < 24 -> PLAYTIME.Q3.time
            else -> PLAYTIME.Q4.time
         }
      }
   }

val ORIENTATIONS: SparseIntArray
   get() {
      val intArray = SparseIntArray()
      intArray.append(Surface.ROTATION_0, 90)
      intArray.append(Surface.ROTATION_90, 0)
      intArray.append(Surface.ROTATION_180, 270)
      intArray.append(Surface.ROTATION_270, 180)
      return intArray
   }

enum class PLAYTIME(val time: String) {
   Q1("morning"),
   Q2("afternoon"),
   Q3("evening"),
   Q4("latenight")
}

val DOWNLOADS_DIR: String = Environment
   .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
   .toString()
val PICTURES_DIR = Environment
   .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
   .toString()