package com.youtise.player.data.repository

import com.youtise.player.data.db.models.DeviceModel
import com.youtise.player.data.repository.datasources.LocationsDataSource
import com.youtise.player.internal.getDeviceSerialNumber
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DeviceRepositoryImpl(
      private val deviceModel: DeviceModel,
      locationsDataSource: LocationsDataSource
) : DeviceRepository {

   private var listener: DeviceRepository.AuthenticationStatusListener? = null
   private val serialNumber by lazy { getDeviceSerialNumber() }

   init {
      locationsDataSource.loginResponse.observeForever {
         persistFetchedToken(it.token, it.playerId)
      }
   }

   private fun persistFetchedToken(authToken: String, playerId: String) {
      GlobalScope.launch(Dispatchers.IO) {
         deviceModel.authToken = authToken
         deviceModel.playerId = playerId
         deviceModel.serialNumber = serialNumber
         listener?.onStatusChanged(true)
      }
   }

   override fun setOnAuthStatusChangedListener(authenticationStatusListener: DeviceRepository.AuthenticationStatusListener) {
      this.listener = authenticationStatusListener
   }
}
