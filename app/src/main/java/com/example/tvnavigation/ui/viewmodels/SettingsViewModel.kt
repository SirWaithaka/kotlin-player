package com.example.tvnavigation.ui.viewmodels

import com.example.tvnavigation.data.repository.DeviceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class SettingsViewModel(private val deviceRepository: DeviceRepository): ScopedViewModel() {


   fun getPlayerInformation(): PlayerInformation = runBlocking {
      withContext(Dispatchers.Default) {
         val device = deviceRepository.getDeviceInfo()
         return@withContext PlayerInformation(
            device.registeredEmail,
            device.locationId,
            device.serialNumber
         )
      }
   }

   data class PlayerInformation (
      val email: String,
      val locationId: String,
      val locationName: String
   )
}