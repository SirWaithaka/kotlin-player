package com.youtise.player.camera.internalcamera

import android.content.Context
import android.graphics.ImageFormat
import android.hardware.camera2.*
import android.media.ImageReader
import android.util.Log
import android.util.Size
import android.view.Surface
import com.youtise.player.internal.CompareSizesByArea
import com.youtise.player.internal.ORIENTATIONS
import java.util.*

abstract class DefaultCamera(protected val context: Context)  {

   private val Tag = "DefaultCamera"

   abstract val reader: ImageReader
   abstract var deviceListener: CameraDevice.StateCallback?
   abstract val windowRotation: Int
   abstract var cameraDevice: CameraDevice?
   protected val cameraManager get() = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager

   abstract val cameraId: String

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
      captureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, getOrientation(cameraDevice.id))
      captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)

      return captureRequestBuilder
   }

   protected fun getSurfaceDimensions(cameraId: String = this.cameraId): Pair<Int, Int> {
      val characteristics: CameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId)
      val streamConfigMap = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
      val jpegSizes: Array<Size> = streamConfigMap?.getOutputSizes(ImageFormat.JPEG) ?: arrayOf()

      val largestSize = Collections.max(jpegSizes.toMutableList(), CompareSizesByArea)

      return largestSize.width to largestSize.height
   }

   private fun getOrientation(cameraId: String = this.cameraId): Int {
      val characteristics: CameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId)
      val sensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION)

      Log.d(Tag, "sensor orientation: $sensorOrientation")
      Log.d(Tag, "window orientation: ${this.windowRotation}")

      return ORIENTATIONS[180]
   }
}