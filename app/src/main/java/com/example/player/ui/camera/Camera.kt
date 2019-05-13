package com.example.player.ui.camera

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.ImageFormat
import android.hardware.camera2.*
import android.media.ImageReader
import android.os.Handler
import android.util.Log
import android.view.Surface
import com.example.player.ui.camera.listeners.DeviceStateListener

abstract class Camera(
   context: Context,
   windowRotation: Int,
   val handler: Handler
) : DefaultCamera(context) {

   private val Tag = "Camera"

   override val rotation: Int = windowRotation
   override var deviceListener: CameraDevice.StateCallback? = null
   final override val reader: ImageReader

   init {
      val (width, height) = this.getSurfaceDimensions(this.cameraId)
      reader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1)
   }

   @SuppressLint("MissingPermission")
   fun openCameraAndCaptureImage(cameraId: String = this.cameraId) {

      if (deviceListener != null) {
         Log.d(Tag, "opening camera with Id: $cameraId")
         cameraManager.openCamera(cameraId,
            deviceListener!!, handler)
      }
   }

   override fun buildCaptureRequest(cameraDevice: CameraDevice, surface: Surface): CaptureRequest {
      val captureRequestBuilder = captureRequestBuilder(cameraDevice)
      captureRequestBuilder.addTarget(surface)

      return captureRequestBuilder.build()
   }

   override fun buildCaptureRequest(cameraDevice: CameraDevice, surfaces: List<Surface>): CaptureRequest {
      val captureRequestBuilder = captureRequestBuilder(cameraDevice)
      for (surface in surfaces) {
         captureRequestBuilder.addTarget(surface)
      }

      return captureRequestBuilder.build()
   }

   companion object Instance {

      fun create(context: Context, windowRotation: Int, handler: Handler) : Camera {
         val cameraInstance = object: Camera(context, windowRotation, handler) {}
         cameraInstance.deviceListener = DeviceStateListener(cameraInstance)

         return cameraInstance
      }
   }
}