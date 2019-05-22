package com.example.player.internal

import android.util.Size
import java.lang.Long.signum
import java.lang.StringBuilder
import java.time.format.DateTimeFormatter

const val TIME_FORMAT_PATTERN = "yyyyMMddHHmmss"
val DEFAULT_STORAGE_DIR = PICTURES_DIR

class IllegalFormatException(message: String = "") : IllegalArgumentException(message)

fun timeStampedFileName(
   extension: String,
   pattern: String = TIME_FORMAT_PATTERN,
   storageDir: String = DEFAULT_STORAGE_DIR): String {

   if (!extension.contains("."))
      throw IllegalFormatException("Extension Argument String should contain '.' char")

   val dateTimeFormatter = DateTimeFormatter.ofPattern(pattern)
   return StringBuilder(storageDir)
      .append("/")
      .append(CURRENT_TIME.format(dateTimeFormatter))
      .append(extension)
      .toString()
}

internal object CompareSizesByArea : Comparator<Size> {

   // We cast here to ensure the multiplications won't overflow
   override fun compare(lhs: Size, rhs: Size) =
      signum(lhs.width.toLong() * lhs.height - rhs.width.toLong() * rhs.height)

}