package com.example.player.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.player.data.db.entities.Location
import com.example.player.data.db.models.DeviceModel
import com.example.player.data.repository.DeviceRepository
import com.example.player.data.repository.LocationsRepository
import com.example.player.internal.SingleEvent
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class LocationsViewModel(
      private val locationsRepository: LocationsRepository,
      private val deviceModel: DeviceModel,
      deviceRepository: DeviceRepository
) : ScopedViewModel(), CoroutineScope {

   private val Tag = "LocationsViewModel"

   private val credentials: Credentials = Credentials()
   private lateinit var mSelectedLocation: Location
   private lateinit var mPassword: String

   private var mLocations = MutableLiveData<List<Location>>()
   val locations: LiveData<List<Location>> get() = mLocations
   private val mHasAuthenticated = MutableLiveData<Boolean>()
   val hasAuthenticated: LiveData<Boolean> get() = mHasAuthenticated
   private val mHasValidatedEmail = MutableLiveData<SingleEvent<Boolean>>()
   val hasValidatedEmail: LiveData<SingleEvent<Boolean>> get() = mHasValidatedEmail

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
            mHasValidatedEmail.postValue(SingleEvent(true))
            Log.d(Tag, "Locations have been fetched")
         }
      })
   }

   fun submitEmail(userEmail: String) {
      credentials.email = userEmail
      launch {
         if (userEmail != deviceModel.locationEmail)
            locationsRepository.validateEmail(userEmail)
         else {
            locationsRepository.getLocations(userEmail)
            deviceModel.locationEmail = userEmail
         }
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

   data class Credentials(
      var email: String = "",
      var locationId: String = "",
      var password: String = ""
   )
}