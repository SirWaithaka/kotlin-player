package com.example.player.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.player.data.db.entities.Location
import com.example.player.data.db.models.DeviceModel
import com.example.player.data.repository.DeviceRepository
import com.example.player.data.repository.LocationsRepository
import com.example.player.internal.SingleEvent
import kotlinx.coroutines.*

class LocationsViewModel(
      private val locationsRepository: LocationsRepository,
      private val deviceModel: DeviceModel,
      deviceRepository: DeviceRepository
) : ScopedViewModel(), CoroutineScope {

   private val Tag = "LocationsViewModel"

   private var mLocations = MutableLiveData<List<Location>>()
   val locations: LiveData<List<Location>> get() = mLocations
   private val mHasAuthenticated = MutableLiveData<SingleEvent<Boolean>>()
   val hasAuthenticated: LiveData<SingleEvent<Boolean>> get() = mHasAuthenticated
   private val mHasValidatedEmail = MutableLiveData<SingleEvent<Boolean>>()
   val hasValidatedEmail: LiveData<SingleEvent<Boolean>> get() = mHasValidatedEmail

   init {
      deviceRepository.setOnAuthStatusChangedListener(object: DeviceRepository.AuthenticationStatusListener {
         override fun onStatusChanged(status: Boolean) {
            deviceModel.authStatus = status
            mHasAuthenticated.postValue(SingleEvent(status))
         }
      })
      locationsRepository.setOnLocationsFetchedListener(object: LocationsRepository.LocationsFetchedListener {
         override fun onLocationsFetched(locations: List<Location>) {
            mLocations.postValue(locations)
            mHasValidatedEmail.postValue(SingleEvent(true))
            Log.d(Tag, "Locations have been fetched")
         }
      })
   }

   fun submitEmail(userEmail: String) {
      launch {
         if (userEmail != deviceModel.locationEmail)
            locationsRepository.validateEmail(userEmail)
         else {
            locationsRepository.getLocations(userEmail)
            deviceModel.locationEmail = userEmail
         }
      }
   }

   fun validateCredentials(location: Location, password: String) {

      launch {

         val device = deviceModel.retrieve()
         device.locationId = location.id
         device.locationName = location.placeName
         deviceModel.update(device)

         locationsRepository.authenticate(
            location.id,
            password
         )
      }
   }

   fun getAuthenticationStatus(): Boolean {
      return deviceModel.authStatus
   }
}