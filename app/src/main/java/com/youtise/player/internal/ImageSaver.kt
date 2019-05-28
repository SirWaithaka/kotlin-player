package com.youtise.player.internal

import android.media.Image
import android.util.Log
import com.youtise.player.data.network.apiservices.PlayerApiService
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType
import okhttp3.MultipartBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.ByteBuffer
import okhttp3.RequestBody
import okhttp3.ResponseBody


object ImageSaver {

   private val Tag = "ImageSaver"

   fun save(image: Image, file: File) {

      val buffer: ByteBuffer = image.planes[0].buffer

      val bytes = ByteArray(buffer.remaining())
      buffer.get(bytes)

      var outStream: FileOutputStream? = null
      try {
         outStream = FileOutputStream(file).apply {
            write(bytes)
         }
      } catch (e: IOException) {
         // TODO("Universal Logic to handle errors")
      } finally {
         image.close()
         outStream?.close()

         Log.d(Tag, "image saved")
      }
   }

   fun upload(file: File, advertId: String, service: PlayerApiService) = runBlocking {
      val advertIdParam = RequestBody.create(MediaType.parse("multipart/form-data"), advertId)
      val fileReqBody = RequestBody.create(MediaType.parse("image/jpeg"), file)
      val part = MultipartBody.Part.createFormData("imageToDetect", file.name, fileReqBody)

      try {
         val response: ResponseBody = service.uploadFile(advertIdParam, part)
         Log.d(Tag, response.string())
      } catch (e: Exception) {
         Log.d(Tag, "Error: ${e.message}")
      } finally {
          file.delete()
      }
   }
}