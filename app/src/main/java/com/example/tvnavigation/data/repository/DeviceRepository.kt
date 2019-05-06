package com.example.tvnavigation.data.repository


interface DeviceRepository {

   interface AuthenticationStatusListener {
      fun onStatusChanged(status: Boolean)
   }

   fun setOnAuthStatusChangedListener(authenticationStatusListener: AuthenticationStatusListener)

}