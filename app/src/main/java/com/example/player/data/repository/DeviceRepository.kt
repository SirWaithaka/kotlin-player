package com.example.player.data.repository


interface DeviceRepository {

   interface AuthenticationStatusListener {
      fun onStatusChanged(status: Boolean)
   }

   fun setOnAuthStatusChangedListener(authenticationStatusListener: AuthenticationStatusListener)

}