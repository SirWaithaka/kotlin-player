package com.youtise.player.camera.internalcamera.listeners

import android.hardware.camera2.*

class CameraCaptureCallback(
   private val cameraDevice: CameraDevice
) : CameraCaptureSession.CaptureCallback() {

//   private val Tag = "CameraCaptureCallback"

   override fun onCaptureCompleted(session: CameraCaptureSession, request: CaptureRequest, result: TotalCaptureResult) {
      super.onCaptureCompleted(session, request, result)
      cameraDevice.close()
   }

   override fun onCaptureFailed(session: CameraCaptureSession, request: CaptureRequest, failure: CaptureFailure) {
      super.onCaptureFailed(session, request, failure)
      cameraDevice.close()
   }
}
