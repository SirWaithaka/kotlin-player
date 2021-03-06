package com.youtise.player.ui.viewmodels

import com.youtise.player.data.db.models.DeviceModel
import com.youtise.player.data.repository.AdvertsRepository
import com.youtise.player.internal.getLocalMediaPath
import com.youtise.player.data.db.models.MediaModel
import kotlinx.coroutines.*
import java.io.File

class SettingsViewModel(
   private val advertsRepository: AdvertsRepository,
   private val deviceModel: DeviceModel
): ScopedViewModel() {

//   private val Tag = "SettingsViewModel"

   private fun deleteLocalMedia(mediaList: List<MediaModel>) {
      for (media in mediaList) {
         val file = File(media.localMediaPath)
         file.delete()
      }
   }

   fun getPlayerInformation(): PlayerInformation = runBlocking {
      withContext(super.coroutineContext) {
         val device = deviceModel.retrieve()
         return@withContext PlayerInformation(
            device.locationEmail,
            device.playerId,
            device.locationName
         )
      }
   }

   fun getCurrentPlaylist(): List<MediaModel> = runBlocking {

      withContext(super.coroutineContext) {
         val adverts = advertsRepository.retrieveAdverts()
         val mediaPlaylist = mutableListOf<MediaModel>()
         for (advert in adverts) {

            mediaPlaylist.add(
               MediaModel(
                  advert.id,
                  advert.adName,
                  advert.mediaType,
                  advert.timeOfDay,
                  advert.getLocalMediaPath()
               )
            )
         }
         return@withContext mediaPlaylist
      }
   }

   fun invalidateSession() = runBlocking {
      val resetDeferred = async { deviceModel.clear() }
      val resetAdDeferred = async { advertsRepository.resetAdverts() }
      val deleteMedia = async { deleteLocalMedia(getCurrentPlaylist()) }

      resetAdDeferred.await()
      resetDeferred.await()
      deleteMedia.await()
   }

   fun invalidateAdverts() = runBlocking {
      advertsRepository.resetAdverts()
      deleteLocalMedia(getCurrentPlaylist())
   }


   data class PlayerInformation (
      val email: String,
      val playerId: String,
      val locationName: String
   )
}