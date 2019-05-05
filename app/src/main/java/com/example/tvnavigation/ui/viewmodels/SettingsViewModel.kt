package com.example.tvnavigation.ui.viewmodels

import com.example.tvnavigation.data.repository.AdvertsRepository
import com.example.tvnavigation.data.repository.DeviceRepository
import com.example.tvnavigation.internal.getLocalMediaPath
import com.example.tvnavigation.ui.models.MediaModel
import kotlinx.coroutines.*
import java.io.File

class SettingsViewModel(
   private val advertsRepository: AdvertsRepository,
   private val deviceRepository: DeviceRepository
): ScopedViewModel() {

   private var currentPlaylist = listOf<MediaInformation>()

   private fun deleteLocalMedia(mediaList: List<MediaInformation>) {
      for (media in mediaList) {
         val file = File(media.mediaLocalPath)
         file.delete()
      }
   }

   fun getPlayerInformation(): PlayerInformation = runBlocking {
      withContext(super.coroutineContext) {
         val device = deviceRepository.getDeviceInfo()
         return@withContext PlayerInformation(
            device.locationEmail,
            device.playerId,
            device.locationName
         )
      }
   }

   fun getCurrentPlaylist(): List<MediaInformation> = runBlocking {

      if (currentPlaylist.isNotEmpty()) return@runBlocking currentPlaylist

      withContext(super.coroutineContext) {
         val adverts = advertsRepository.retrieveAdverts()
         val mediaPlaylist = mutableListOf<MediaInformation>()
         for (advert in adverts) {

            mediaPlaylist.add(
               MediaInformation(
                  advert.adName,
                  advert.mediaType,
                  advert.timeOfDay,
                  advert.getLocalMediaPath()
               )
            )
         }
         currentPlaylist = mediaPlaylist
         return@withContext mediaPlaylist
      }
   }

   fun invalidateSession() = runBlocking {
      val resetDeferred = async { deviceRepository.getDeviceModel().clear() }
      val resetAdDeferred = async { advertsRepository.resetAdverts() }
      val deleteMedia = async { deleteLocalMedia(getCurrentPlaylist()) }

      resetAdDeferred.await()
      resetDeferred.await()
      deleteMedia.await()
   }

   fun invalidateAdverts() = runBlocking {
      val resetAdDeferred = async { advertsRepository.resetAdverts() }
      deleteLocalMedia(getCurrentPlaylist())

      resetAdDeferred.await()
   }

   data class MediaInformation(
      override val name: String,
      override val type: String,
      override val timesOfDay: List<String>,
      override val mediaLocalPath: String
   ): MediaModel()

   data class PlayerInformation (
      val email: String,
      val playerId: String,
      val locationName: String
   )
}