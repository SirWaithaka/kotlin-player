package com.youtise.player.camera.internalcamera.listeners

import android.media.ImageReader
import com.youtise.player.data.network.apiservices.PlayerApiService
import com.youtise.player.internal.ImageSaver
import com.youtise.player.internal.timeStampedFileName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File


class ImageCapturedListener(
   private val id: String,
   private val service: PlayerApiService
): ImageReader.OnImageAvailableListener {

//   private val Tag = "ImageCapturedListener"


   override fun onImageAvailable(reader: ImageReader) {

      val file = File(timeStampedFileName(extension = ".jpg"))
      GlobalScope.launch(Dispatchers.IO) {
         ImageSaver.save(reader.acquireLatestImage(), file)
         ImageSaver.upload(file, id, service)
      }
   }
}