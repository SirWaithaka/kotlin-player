package com.example.player.internal

import android.annotation.SuppressLint
import android.os.Build
import android.util.Size
import org.threeten.bp.format.DateTimeFormatter
import java.lang.Long.signum
import java.lang.StringBuilder

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

@Suppress("DEPRECATION")
@SuppressLint("HardwareIds")
fun getDeviceSerialNumber(): String {

   return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      try { Build.getSerial() }
      catch (e: SecurityException) { "" }
   }
   else
      Build.SERIAL
}

internal object CompareSizesByArea : Comparator<Size> {

   // We cast here to ensure the multiplications won't overflow
   override fun compare(lhs: Size, rhs: Size) =
      signum(lhs.width.toLong() * lhs.height - rhs.width.toLong() * rhs.height)

}