package com.example.tvnavigation.data.repository

import com.example.tvnavigation.data.db.entities.Device

interface DeviceRepository {
   interface AuthenticationStatusListener {
      fun onStatusChanged(status: Boolean)
   }
   fun setOnAuthStatusChangedListener(authenticationStatusListener: AuthenticationStatusListener)

   suspend fun getDeviceInfo(): Device
}