package com.youtise.player.data.repository.datasources

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.youtise.player.data.network.AdvertLog
import com.youtise.player.data.network.responses.AdvertisementsResponse
import com.youtise.player.data.network.apiservices.PlayerApiService
import com.youtise.player.internal.ClientErrorException
import com.youtise.player.internal.NoConnectivityException
import com.youtise.player.internal.ServerErrorException

class AdvertsNetworkDataSourceImpl(
   private val playerApiService: PlayerApiService
) : AdvertsNetworkDataSource {

   private val _downloadedAdverts = MutableLiveData<AdvertisementsResponse>()
   override val downloadedAdverts: LiveData<AdvertisementsResponse>
      get() = _downloadedAdverts

   private val _httpErrorResponse = MutableLiveData<String>()
   override val httpErrorResponse: LiveData<String>
      get() = _httpErrorResponse

   override suspend fun fetchCurrentAds() {
      try {
         val fetchedLocations = playerApiService.fetchCurrentAdverts()
         _downloadedAdverts.postValue(fetchedLocations)
      } catch (e: Exception) {
         handleExceptions(e)
      }
   }

   override suspend fun postAdvertLog(log: AdvertLog) {
      try {
         playerApiService.onAdvertChange(
            id = log.id,
            startTime = log.start,
            endTime = log.end,
            result = log.result
         )
      } catch (e: Exception) {
         handleExceptions(e)
      }
   }

   override suspend fun invokePopCapture(advertId: String) {
      try {
         playerApiService.invokePopCapture(advertId)
      } catch (e: Exception) {
         handleExceptions(e)
      }
   }

   private fun handleExceptions(e: Exception) {
      when (e) {
         is NoConnectivityException -> _httpErrorResponse.postValue("No internet Connection")
         is ClientErrorException -> _httpErrorResponse.postValue(e.message)
         is ServerErrorException -> _httpErrorResponse.postValue(e.message)
         else -> _httpErrorResponse.postValue(e.message)
      }
   }
}