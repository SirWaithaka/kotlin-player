package com.example.player.ui.hardware

import android.app.Activity
import android.content.Context
import android.graphics.ImageFormat
import android.hardware.camera2.*
import android.media.ImageReader
import android.util.Size
import android.view.Surface
import com.example.player.internal.IMAGE_HEIGHT
import com.example.player.internal.IMAGE_WIDTH
import com.example.player.internal.ORIENTATIONS

abstract class Camera(private val activity: Activity) {

   protected val context: Context = activity.applicationContext
   protected var cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
   val cameraId: String
      get() {
         return try {
            val cameraIds: Array<String> = cameraManager.cameraIdList
            if (cameraIds.isNotEmpty()) cameraIds[0] else ""
         } catch (e: CameraAccessException) {
            ""
         }
      }

   abstract val reader: ImageReader

   protected data class SurfaceDimensions(
      val width: Int,
      val height: Int
   )

   abstract fun buildCaptureRequest(cameraDevice: CameraDevice, surface: Surface) : CaptureRequest

   protected fun getOrientation(): Int {
      val rotation = activity.windowManager.defaultDisplay.rotation
      return ORIENTATIONS[rotation]
   }

   protected fun getSurfaceDimensions(cameraId: String): SurfaceDimensions {
      val characteristics: CameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId)
      val streamConfigMap = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
      val jpegSizes: Array<Size> = streamConfigMap?.getOutputSizes(ImageFormat.JPEG) ?: arrayOf()

      val width: Int = if (jpegSizes.isNotEmpty()) jpegSizes[0].width else IMAGE_WIDTH
      val height: Int = if (jpegSizes.isNotEmpty()) jpegSizes[0].height else IMAGE_HEIGHT

      return SurfaceDimensions(width, height)
   }
}