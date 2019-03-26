package com.example.tvnavigation.data.repository.datasources

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tvnavigation.data.network.responses.LocationsResponse
import com.example.tvnavigation.data.network.services.LocationsService
import com.example.tvnavigation.internal.NoConnectivityException
import java.lang.Exception

class LocationsDataSourceImpl(
      private val locationsApiService: LocationsService
) : LocationsDataSource {

   private val TAG = "LocationsDataSource"

   private val _downloadedLocations = MutableLiveData<LocationsResponse>()
   override val downloadedLocations: LiveData<LocationsResponse>
         get() = _downloadedLocations

   override suspend fun fetchLocations(email: String) {
      try {
         val fetchedLocations = locationsApiService.getLocationsByEmail(email).await()
         _downloadedLocations.postValue(fetchedLocations)
      } catch (e: Exception) {
         when (e) {
            is NoConnectivityException -> Log.d(TAG, "Player Fragment: No internet Connection")
            else -> Log.d(TAG, "Player Fragment:", e)
         }
      }
   }
}