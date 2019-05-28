package com.youtise.player.camera.internalcamera.listeners

import android.hardware.camera2.CameraDevice
import android.view.Surface
import com.youtise.player.camera.internalcamera.Camera
import android.util.Log


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
      Log.d(Tag, "Error opening camera: $error")
   }

   override fun onClosed(camera: CameraDevice) {
      super.onClosed(camera)

      Log.d(Tag, "Device closed")
   }
}