package com.example.player.ui.camera.listeners

import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CaptureRequest
import android.hardware.camera2.TotalCaptureResult

class CameraCaptureCallback(
   private val cameraDevice: CameraDevice
) : CameraCaptureSession.CaptureCallback() {

//   private val Tag = "CameraCaptureCallback"

   override fun onCaptureCompleted(session: CameraCaptureSession, request: CaptureRequest, result: TotalCaptureResult) {
      super.onCaptureCompleted(session, request, result)
      cameraDevice.close()
   }
}
