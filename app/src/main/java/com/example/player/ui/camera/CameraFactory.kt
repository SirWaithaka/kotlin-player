package com.example.player.ui.hardware

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.ImageFormat
import android.hardware.camera2.*
import android.media.ImageReader
import android.view.Surface

class CameraSingleton(activity: Activity): Camera(activity) {


   private val cameraInstance: Camera = this

   init {
       openCamera(this.cameraId)
   }

   override val reader: ImageReader
      get() {
         val (width, height) = getSurfaceDimensions(this.cameraId)
         return ImageReader.newInstance(width, height, ImageFormat.JPEG, 1)
      }
//   reader.setOnImageAvailableListener(listener, null)


   @SuppressLint("MissingPermission")
   private fun openCamera(cameraId: String) {
      cameraManager.openCamera(cameraId, CameraStateListener(cameraInstance), null)
   }



}