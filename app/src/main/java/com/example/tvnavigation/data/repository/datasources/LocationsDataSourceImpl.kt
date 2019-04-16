package com.example.tvnavigation.data.repository.datasources

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tvnavigation.data.network.responses.LocationsResponse
import com.example.tvnavigation.data.network.services.AuthorizationService
import com.example.tvnavigation.data.network.services.LocationsService
import com.example.tvnavigation.internal.ClientErrorException
import com.example.tvnavigation.internal.NoConnectivityException
import com.example.tvnavigation.internal.ServerErrorException
import java.lang.Exception

class LocationsDataSourceImpl(
      private val locationsService: LocationsService,
      private val authorizationService: AuthorizationService
) : LocationsDataSource {

   private val _downloadedLocations = MutableLiveData<LocationsResponse>()
   override val downloadedLocations: LiveData<LocationsResponse>
         get() = _downloadedLocations
   private val _httpErrorResponse = MutableLiveData<String>()
   override val httpErrorResponse: LiveData<String>
         get() = _httpErrorResponse
   private val _authToken = MutableLiveData<String>()
   override val authToken: LiveData<String>
         get() = _authToken
   private val _isAuthenticated = MutableLiveData<Boolean>()
   override val isAuthenticated: LiveData<Boolean>
      get() = _isAuthenticated


   override suspend fun fetchLocations(email: String) {
      try {
         val fetchedLocations = locationsService.getLocationsByEmail(email)
         _downloadedLocations.postValue(fetchedLocations)
      } catch (e: Exception) {
         handleExceptions(e)
      }
   }

   override suspend fun authenticate(id: String, serialNumber: String, password: String) {
      try {
         val fetchedToken = authorizationService.authenticateUser(id, serialNumber, password)
         _authToken.postValue(fetchedToken.token)
         _isAuthenticated.postValue(true)
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