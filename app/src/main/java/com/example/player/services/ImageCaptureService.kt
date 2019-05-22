package com.example.player.services

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.WindowManager
import androidx.core.app.NotificationCompat
import com.example.player.R
import com.example.player.data.network.apiservices.PlayerApiService
import com.example.player.internal.CHANNEL_ID
import com.example.player.ui.camera.Camera
import com.example.player.ui.camera.listeners.ImageCapturedListener
import kotlinx.coroutines.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class ImageCaptureService : IntentService("Image Capture"), KodeinAware {

   private val Tag = "ImageCaptureService"

   override val kodein: Kodein by kodein()
   private var handler: Handler? = null
   private var cameraInstance: Camera? = null
   private lateinit var windowManager: WindowManager
   private val service: PlayerApiService by instance()

   init {
       setIntentRedelivery(true)
   }


   override fun onCreate() {
      super.onCreate()

      Log.d(Tag, "onCreate")
      handler = Handler(Looper.myLooper())

      windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
         val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Capturing Image")
            .setContentText("Running ...")
            .setSmallIcon(R.drawable.ic_refresh)
            .build()

         startForeground(1, notification)
      }
   }
   override fun onHandleIntent(intent: Intent?) = runBlocking {
      Log.d(Tag, "onHandleIntent")

      val id = intent?.getStringExtra("advertId")

      try {
         Log.d(Tag, "Instantiating camera instance")
         cameraInstance = Camera.create(applicationContext, windowManager.defaultDisplay.rotation, handler!!)
         cameraInstance?.reader?.setOnImageAvailableListener(ImageCapturedListener(id!!, service), handler)
         cameraInstance?.openCameraAndCaptureImage()
      } catch (e: Exception) {
         Log.e(Tag, "Camera access exception: ${e.message} : ${e.printStackTrace()}")
      }

      delay(10000)
   }

   override fun onDestroy() {
      super.onDestroy()

      cameraInstance?.cameraDevice?.close()
      cameraInstance = null
      handler = null
      Log.d(Tag, "serviceFinished")
   }
}