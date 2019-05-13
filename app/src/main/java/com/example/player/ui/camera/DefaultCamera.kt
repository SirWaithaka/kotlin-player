package com.example.player.ui.camera

import android.content.Context
import android.graphics.ImageFormat
import android.hardware.camera2.*
import android.media.ImageReader
import android.util.Log
import android.util.Size
import android.view.Surface
import com.example.player.internal.IMAGE_HEIGHT
import com.example.player.internal.IMAGE_WIDTH
import com.example.player.internal.ORIENTATIONS
import java.util.*

abstract class DefaultCamera(protected val context: Context)  {

   private val Tag = "DefaultCamera"

   abstract val reader: ImageReader
   abstract var deviceListener: CameraDevice.StateCallback?
   abstract val rotation: Int

   protected val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager

   protected val cameraId: String
      get() {
         return try {
            val cameraIds: Array<String> = cameraManager.cameraIdList
            if (cameraIds.isNotEmpty()) cameraIds[0] else ""
         } catch (e: CameraAccessException) {
            ""
         }
      }

   abstract fun buildCaptureRequest(
      cameraDevice: CameraDevice,
      surface: Surface = this.reader.surface): CaptureRequest

   abstract fun buildCaptureRequest(
      cameraDevice: CameraDevice,
      surfaces: List<Surface>): CaptureRequest

   protected fun captureRequestBuilder(cameraDevice: CameraDevice) : CaptureRequest.Builder {

      val captureRequestBuilder: CaptureRequest.Builder =
         cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
      captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO)
      captureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, getOrientation())

      return captureRequestBuilder
   }

   protected fun getSurfaceDimensions(cameraId: String = this.cameraId): Pair<Int, Int> {
      val characteristics: CameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId)
      val streamConfigMap = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
      val jpegSizes: Array<Size> = streamConfigMap?.getOutputSizes(ImageFormat.JPEG) ?: arrayOf()

      val width: Int = if (jpegSizes.isNotEmpty()) jpegSizes[0].width else IMAGE_WIDTH
      val height: Int = if (jpegSizes.isNotEmpty()) jpegSizes[0].height else IMAGE_HEIGHT

      return width to height
   }

   private fun getOrientation(): Int {
      return ORIENTATIONS[this.rotation]
   }

//   companion object {
//
//      private val Tag = "DefaultCamera"
//
//      fun chooseOptimalSize(
//         choices: Array<Size>, textureViewWidth: Int,
//         textureViewHeight: Int, maxWidth: Int, maxHeight: Int, aspectRatio: Size
//      ): Size {
//
//         // Collect the supported resolutions that are at least as big as the preview Surface
//         val bigEnough = mutableListOf<Size>()
//         // Collect the supported resolutions that are smaller than the preview Surface
//         val notBigEnough = mutableListOf<Size>()
//         val w = aspectRatio.width
//         val h = aspectRatio.height
//         for (option in choices) {
//            if (option.width <= maxWidth && option.height <= maxHeight &&
//               option.height == option.width * h / w
//            ) {
//               if (option.width >= textureViewWidth && option.height >= textureViewHeight) {
//                  bigEnough.add(option)
//               } else {
//                  notBigEnough.add(option)
//               }
//            }
//         }
//
//         // Pick the smallest of those big enough. If there is no one big enough, pick the
//         // largest of those not big enough.
//         if (bigEnough.size > 0) {
//            return Collections.min(bigEnough, CompareSizesByArea())
//         } else if (notBigEnough.size > 0) {
//            return Collections.max(notBigEnough, CompareSizesByArea())
//         } else {
//            Log.e(Tag, "Couldn't find any suitable preview size")
//            return choices[0]
//         }
//      }
//   }
}