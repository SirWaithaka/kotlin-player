package com.example.tvnavigation.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tvnavigation.data.db.entities.Location
import com.example.tvnavigation.data.repository.LocationsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class LocationsViewModel(
      private val locationsRepository: LocationsRepository
) : ViewModel(), CoroutineScope {

   private val job = Job()
   private lateinit var mSelectedLocation: Location
   private lateinit var mPassword: String
   var mLocations = MutableLiveData<List<Location>>()
   val locations: LiveData<List<Location>>
         get() = mLocations
   override val coroutineContext: CoroutineContext
      get() = job + Dispatchers.IO

   val httpErrorResponse: LiveData<String> = locationsRepository.getHttpErrorResponses()

   fun validateEmail(userEmail: String) {
      var fetchedLocations: List<Location>
      launch {
         fetchedLocations = locationsRepository.getLocations(userEmail)
         Log.d("ViewModel", "Fetched Locations: $fetchedLocations")
         mLocations.postValue(fetchedLocations)
         Log.d("ViewModel", "${mLocations.value}")
      }
   }

   fun locationFragmentCalling() {
      Log.d("ViewModel", "location fragment calling")
   }

   fun setSelectedLocation(selectedLocation: Location) {
      mSelectedLocation = selectedLocation
   }
   fun setInputPassword(password: String) {
      mPassword = password
   }
   fun validateCredentials() {
      val fetchedAds = locationsRepository.getCurrentAds(mSelectedLocation.id, mPassword)
      //launch {

      //}
      Log.d("ViewModel", "${mSelectedLocation.id}:$mPassword")
   }
}