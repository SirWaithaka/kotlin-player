package com.youtise.player.services

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.WindowManager
import com.youtise.player.camera.internalcamera.Camera
import com.youtise.player.camera.internalcamera.listeners.ImageCapturedListener
import com.youtise.player.data.network.apiservices.PlayerApiService
import com.youtise.player.internal.looperCoroutine
import com.youtise.player.internal.looperThread
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import kotlin.concurrent.thread


class CaptureImage(private val context: Context): KodeinAware {
   private val Tag = "CaptureImage"

   override val kodein: Kodein by kodein(context)

   private var thread: Thread? = null
   private var handler: Handler? = null
   private var cameraInstance: Camera? = null
   private val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
   private val service: PlayerApiService by instance()

   operator fun invoke(id: String) = runBlocking(Dispatchers.IO) {
      val job = looperCoroutine {
         try {
            handler = Handler(Looper.myLooper())

            cameraInstance = Camera.create(context, windowManager.defaultDisplay.rotation, handler!!)
            cameraInstance?.reader?.setOnImageAvailableListener(ImageCapturedListener(cameraInstance!!, id, service), handler)
            cameraInstance?.openCameraAndCaptureImage()
         } catch (e: Exception) {
            Log.e(Tag, "Error taking picture: ${e.message} : ${e.printStackTrace()}")
         }
      }
      Log.d(Tag, "job is active: ${job.isActive}")
   }

   fun destroy() {
      cameraInstance = null
      handler = null
      thread = null
      Log.d(Tag, "serviceFinished")
   }
}