package com.example.tvnavigation.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tvnavigation.data.db.entities.Location
import com.example.tvnavigation.data.db.models.DeviceModel
import com.example.tvnavigation.data.repository.DeviceRepository
import com.example.tvnavigation.data.repository.LocationsRepository
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class LocationsViewModel(
      private val locationsRepository: LocationsRepository,
      private val deviceModel: DeviceModel,
      deviceRepository: DeviceRepository
) : ViewModel(), CoroutineScope {

//   private val Tag = "LocationsViewModel"
   private val job = Job()
   private lateinit var mSelectedLocation: Location
   private lateinit var mPassword: String
   private var mLocations = MutableLiveData<List<Location>>()
   val locations: LiveData<List<Location>>
         get() = mLocations
   override val coroutineContext: CoroutineContext
         get() = job + Dispatchers.IO

   private val mHasAuthenticated = MutableLiveData<Boolean>()
   val hasAuthenticated: LiveData<Boolean>
         get() = mHasAuthenticated

   init {
      deviceRepository.setOnAuthStatusChangedListener(object: DeviceRepository.AuthenticationStatusListener {
         override fun onStatusChanged(status: Boolean) {
            deviceModel.authStatus = status
            mHasAuthenticated.postValue(status)
         }
      })
      locationsRepository.setOnLocationsFetchedListener(object: LocationsRepository.LocationsFetchedListener {
         override fun onLocationsFetched(locations: List<Location>) {
            mLocations.postValue(locations)
         }
      })
   }

   fun validateEmail(userEmail: String) {
      launch {
         locationsRepository.getLocationsByEmail(userEmail)
         deviceModel.locationEmail = userEmail
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

         val device = deviceModel.retrieve()
         device.locationId = mSelectedLocation.id
         device.locationName = mSelectedLocation.placeName
         deviceModel.update(device)

         locationsRepository.authenticate(
            mSelectedLocation.id,
            mPassword
         )
      }
   }

   fun getAuthenticationStatus(): Boolean {
      return deviceModel.authStatus
   }
}