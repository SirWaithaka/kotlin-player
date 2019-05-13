package com.example.player.services

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.media.ImageReader
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import android.view.WindowManager
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.player.R
import com.example.player.internal.CHANNEL_ID
import com.example.player.ui.camera.Camera
import com.example.player.ui.camera.listeners.ImageCapturedListener
import kotlinx.coroutines.*
import java.lang.Runnable

class ImageCaptureService : IntentService("Image Capture") {

   private val Tag = "ImageCaptureService"

   private var handler: Handler? = null
   private lateinit var windowManager: WindowManager

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

      val input = intent?.getStringExtra("inputExtra")

      Log.d(Tag, "Instantiating camera instance")
      val cameraInstance = Camera.create(applicationContext, windowManager.defaultDisplay.rotation, handler!!)
      cameraInstance.reader.setOnImageAvailableListener(ImageCapturedListener(), handler)
      cameraInstance.openCameraAndCaptureImage()

      delay(10000)
   }

   override fun onDestroy() {
      super.onDestroy()

      handler = null
      Log.d(Tag, "serviceFinished")
   }
}