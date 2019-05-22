package com.example.player.ui.camera.listeners

import android.hardware.camera2.CameraDevice
import android.view.Surface
import com.example.player.ui.camera.Camera
import android.graphics.SurfaceTexture
import android.util.Log
import com.example.player.internal.IMAGE_HEIGHT
import com.example.player.internal.IMAGE_WIDTH


class DeviceStateListener(private val camera: Camera) : CameraDevice.StateCallback() {

   private val Tag = "DeviceStateListener"

   private val surface: Surface get() = camera.reader.surface


   /*
    * When the camera is opened
    * 1. Get Build the session camera request
    * 2. take the picture
    */
   override fun onOpened(cameraDevice: CameraDevice) {
      camera.cameraDevice = cameraDevice
      val sessionRequest = camera.buildCaptureRequest(cameraDevice)

      Log.d(Tag, "Camera opened")
      cameraDevice.createCaptureSession(listOf(surface),
         CaptureSessionListener(sessionRequest, cameraDevice), camera.handler)
   }

   override fun onDisconnected(cameraDevice: CameraDevice) {
      cameraDevice.close()
   }

   override fun onError(cameraDevice: CameraDevice, error: Int) {
      cameraDevice.close()
      Log.d(Tag, "Error opening camera: $error")
   }

   override fun onClosed(camera: CameraDevice) {
      super.onClosed(camera)

      Log.d(Tag, "Device closed")
   }
}