package com.example.tvnavigation.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tvnavigation.data.db.entities.Location
import com.example.tvnavigation.data.repository.DeviceRepository
import com.example.tvnavigation.data.repository.DeviceRepositoryImpl
import com.example.tvnavigation.data.repository.LocationsRepository
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class LocationsViewModel(
      private val locationsRepository: LocationsRepository,
      private val deviceRepository: DeviceRepository
) : ViewModel(), CoroutineScope {

   private val job = Job()
   private lateinit var mSelectedLocation: Location
   private lateinit var mPassword: String
   private var mLocations = MutableLiveData<List<Location>>()
   val locations: LiveData<List<Location>>
         get() = mLocations
   override val coroutineContext: CoroutineContext
         get() = job + Dispatchers.IO

   val httpErrorResponse: LiveData<String> = locationsRepository.getHttpErrorResponses()
   private val mHasAuthenticated = MutableLiveData<Boolean>()
   val hasAuthenticated: LiveData<Boolean>
         get() = mHasAuthenticated

   init {
      deviceRepository.setOnAuthStatusChangedListener(object: DeviceRepository.AuthenticationStatusListener {
         override fun onStatusChanged(status: Boolean) {
            Log.d("ViewModel", "Listener status: $status")
            mHasAuthenticated.postValue(status)
         }
      })
   }

   fun validateEmail(userEmail: String) {
      var fetchedLocations: List<Location>
      launch {
         fetchedLocations = locationsRepository.getLocations(userEmail)
         Log.d("ViewModel", "Fetched Locations: $fetchedLocations")
         mLocations.postValue(fetchedLocations)
      }
   }
   fun setSelectedLocation(selectedLocation: Location) {
      mSelectedLocation = selectedLocation
   }
   fun setInputPassword(password: String) {
      mPassword = password
   }
   fun validateCredentials() {
      launch {
         withContext(Dispatchers.Default) {
            locationsRepository.authenticate(
               mSelectedLocation.id,
               mPassword
            )
         }
      }
      Log.d("ViewModel", "Not Blocking")
   }

   fun setAuthenticationStatus(authStatus: Boolean) {
      mHasAuthenticated.postValue(authStatus)
   }

   suspend fun getAuthenticationStatus(): Boolean {
      val device = deviceRepository.getDeviceInfo()
      return device.authStatus
   }
}