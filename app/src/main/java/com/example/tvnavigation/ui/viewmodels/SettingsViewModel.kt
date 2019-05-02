package com.example.tvnavigation.ui.viewmodels

import com.example.tvnavigation.data.repository.AdvertsRepository
import com.example.tvnavigation.data.repository.DeviceRepository
import kotlinx.coroutines.*

class SettingsViewModel(
   private val advertsRepository: AdvertsRepository,
   private val deviceRepository: DeviceRepository
): ScopedViewModel() {

//   private val TAG = "SettingsViewModel"

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

   fun getCurrentPlaylist(): List<MediaInformation> = runBlocking {
      withContext(Dispatchers.Default) {
         val adverts = advertsRepository.retrieveAdverts()
         val mediaPlaylist = mutableListOf<MediaInformation>()
         for (advert in adverts) {
            mediaPlaylist.add(
               MediaInformation(advert.adName, advert.mediaType, advert.timeOfDay)
            )
         }
         return@withContext mediaPlaylist
      }
   }

   fun invalidateSession() = runBlocking(super.coroutineContext) {
      val resetDeferred = async { deviceRepository.resetDevice() }
      val resetAdDeferred = async { advertsRepository.resetAdverts() }

      resetAdDeferred.await().plus(resetDeferred.await())

   }

   data class MediaInformation(
      val name: String,
      val type: String,
      val timesOfDay: List<String>
   )

   data class PlayerInformation (
      val email: String,
      val locationId: String,
      val locationName: String
   )
}