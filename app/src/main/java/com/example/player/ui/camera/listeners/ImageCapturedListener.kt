package com.example.player.ui.camera.listeners

import android.media.Image
import android.media.ImageReader
import android.util.Log
import com.example.player.internal.CURRENT_TIME
import com.example.player.internal.PICTURES_DIR
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.lang.StringBuilder
import java.nio.ByteBuffer
import java.time.format.DateTimeFormatter

class ImageCapturedListener : ImageReader.OnImageAvailableListener {

   private val Tag = "ImageCapturedListener"


   override fun onImageAvailable(reader: ImageReader) {
      Log.d(Tag, "Image captured")

      val image: Image = reader.acquireLatestImage()
      val buffer: ByteBuffer = image.planes[0].buffer

      val bytes = ByteArray(buffer.remaining())
      buffer.get(bytes)

      val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
      val filePath: String = StringBuilder(PICTURES_DIR)
         .append("/")
         .append(CURRENT_TIME.format(dateTimeFormatter))
         .append(".jpg")
         .toString()

      Log.d(Tag, filePath)
      val file = File(filePath)
      var outStream: FileOutputStream? = null

      try {
         outStream = FileOutputStream(file)
         outStream.write(bytes)
      } catch(e: IOException) {
         Log.d(Tag, e.printStackTrace().toString())
      } finally {
         Log.d(Tag, "image has been written")
         image.close()
         outStream?.close()
      }
   }
}