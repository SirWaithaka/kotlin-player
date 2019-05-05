package com.example.tvnavigation.data.repository

import com.example.tvnavigation.data.db.entities.Device
import com.example.tvnavigation.data.db.models.DeviceModel
import java.time.ZonedDateTime

interface DeviceRepository {

   interface AuthenticationStatusListener {
      fun onStatusChanged(status: Boolean)
   }


   fun getDeviceModel(): DeviceModel
   fun setOnAuthStatusChangedListener(authenticationStatusListener: AuthenticationStatusListener)
//   fun setLastUpdated(date: ZonedDateTime)
//   fun setLocationId(id: String)

   suspend fun getDeviceInfo(): Device
//   suspend fun resetDevice(): Int
//   suspend fun setEmail(email: String)
//   suspend fun updateNameAndId(placeId: String, placeName: String)
}