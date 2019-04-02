package com.example.tvnavigation.data.repository.datasources

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tvnavigation.data.network.responses.AdvertisementsResponse
import com.example.tvnavigation.data.network.services.PlayerService
import com.example.tvnavigation.internal.ClientErrorException
import com.example.tvnavigation.internal.NoConnectivityException
import com.example.tvnavigation.internal.ServerErrorException

class AdvertsNetworkDataSourceImpl(
      private val playerApiService: PlayerService
) : AdvertsNetworkDataSource {

   private  val _downloadedLocations = MutableLiveData<AdvertisementsResponse>()
   override val downloadedLocations: LiveData<AdvertisementsResponse>
      get() = _downloadedLocations

   private  val _httpErrorResponse = MutableLiveData<String>()
   override val httpErrorResponse: LiveData<String>
      get() = _httpErrorResponse

   override suspend fun fetchCurrentAds() {
      try {
         val fetchedLocations = playerApiService.fetchCurrentAdverts()
         _downloadedLocations.postValue(fetchedLocations)
      } catch (e: Exception) {
         when (e) {
            is NoConnectivityException -> _httpErrorResponse.postValue("No internet Connection")
            is ClientErrorException -> _httpErrorResponse.postValue(e.message)
            is ServerErrorException -> _httpErrorResponse.postValue(e.message)
            else -> _httpErrorResponse.postValue(e.message)
         }
      }
   }
}