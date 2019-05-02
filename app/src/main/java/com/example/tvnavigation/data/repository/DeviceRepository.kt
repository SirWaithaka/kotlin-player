package com.example.tvnavigation.data.repository

import com.example.tvnavigation.data.db.entities.Device
import java.time.ZonedDateTime

interface DeviceRepository {
   interface AuthenticationStatusListener {
      fun onStatusChanged(status: Boolean)
   }
   fun setOnAuthStatusChangedListener(authenticationStatusListener: AuthenticationStatusListener)
   fun setLastUpdated(date: ZonedDateTime)
   fun setLocationId(id: String)

   suspend fun getDeviceInfo(): Device
   suspend fun resetDevice(): Int
}