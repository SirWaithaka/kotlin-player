package com.example.tvnavigation.data.repository.datasources

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tvnavigation.data.network.responses.LocationsResponse
import com.example.tvnavigation.data.network.services.LocationsService
import com.example.tvnavigation.internal.ClientErrorException
import com.example.tvnavigation.internal.NoConnectivityException
import com.example.tvnavigation.internal.ServerErrorException
import java.lang.Exception

class LocationsDataSourceImpl(
      private val locationsApiService: LocationsService
) : LocationsDataSource {

   private val _downloadedLocations = MutableLiveData<LocationsResponse>()
   override val downloadedLocations: LiveData<LocationsResponse>
         get() = _downloadedLocations
   private val _httpErrorResponse = MutableLiveData<String>()
   override val httpErrorResponse: LiveData<String>
         get() = _httpErrorResponse


   override suspend fun fetchLocations(email: String) {
      try {
         val fetchedLocations = locationsApiService.getLocationsByEmail(email)
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