package com.example.player.ui.hardware

import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CaptureRequest
import android.hardware.camera2.TotalCaptureResult

class CameraSessionStateListener(private val captureRequest: CaptureRequest) : CameraCaptureSession.StateCallback() {

   // TODO("Implement other functions")
   override fun onConfigured(session: CameraCaptureSession) {
      try {
          session.capture(captureRequest, )
      } catch ()
   }

   override fun onConfigureFailed(session: CameraCaptureSession) {

   }

   private val captureListener = object : CameraCaptureSession.CaptureCallback() {
      override fun onCaptureCompleted(
         session: CameraCaptureSession,
         request: CaptureRequest,
         result: TotalCaptureResult
      ) {
         super.onCaptureCompleted(session, request, result)


      }
   }
}

//      override fun onCaptureCompleted(
//         session: CameraCaptureSession, request: CaptureRequest,
//         @NonNull result: TotalCaptureResult
//      ) {
//         super.onCaptureCompleted(session, request, result)
//         if (picturesTaken.lastEntry() != null) {
//            capturingListener.onCaptureDone(picturesTaken.lastEntry().key, picturesTaken.lastEntry().value)
//         }
//         closeCamera()
//      }