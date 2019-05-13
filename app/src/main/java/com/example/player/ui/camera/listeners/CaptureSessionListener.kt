package com.example.player.ui.camera.listeners

import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CaptureRequest
import android.util.Log
import android.view.Surface

class CaptureSessionListener(
   private val captureRequest: CaptureRequest
) : CameraCaptureSession.StateCallback() {

   private val Tag = "CaptureSessionListener"

   // TODO("Implement other functions")
   override fun onConfigured(session: CameraCaptureSession) {
      try {
          session.capture(captureRequest, null, null)
      } catch (e: CameraAccessException) {
         Log.d(Tag, "Could not access camera")
      }
   }

   override fun onConfigureFailed(session: CameraCaptureSession) {
      Log.d(Tag, "Could not configure session")
   }

   override fun onSurfacePrepared(session: CameraCaptureSession, surface: Surface) {
      super.onSurfacePrepared(session, surface)

      Log.d(Tag, "Surface prepared")
   }
}