package com.example.player.ui.hardware

import android.hardware.camera2.CameraDevice

class CameraStateListener(private val camera: Camera) : CameraDevice.StateCallback() {

   private val surface = camera.reader.surface


   /*
    * When the camera is opened
    * 1. Get Build the session camera request
    * 2. take the picture
    */
   override fun onOpened(cameraDevice: CameraDevice) {
      val sessionRequest = camera.buildCaptureRequest(cameraDevice, camera.reader.surface)
      cameraDevice.createCaptureSession(listOf(surface), CameraSessionStateListener(sessionRequest), null)
   }

   override fun onClosed(cameraDevice: CameraDevice) {
      super.onClosed(cameraDevice)


   }

   override fun onDisconnected(cameraDevice: CameraDevice) {

   }

   override fun onError(cameraDevice: CameraDevice, error: Int) {

   }
}