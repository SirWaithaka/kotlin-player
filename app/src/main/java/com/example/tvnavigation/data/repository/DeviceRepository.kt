package com.example.tvnavigation.data.repository

import com.example.tvnavigation.data.db.entities.Device
import java.time.ZonedDateTime

interface DeviceRepository {
   interface AuthenticationStatusListener {
      fun onStatusChanged(status: Boolean)
   }
   fun setOnAuthStatusChangedListener(authenticationStatusListener: AuthenticationStatusListener)

   fun setLocationId(id: String)
   suspend fun getDeviceInfo(): Device
   fun setLastUpdated(date: ZonedDateTime)
}