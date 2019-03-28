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
   private val mEmail = MutableLiveData<String>()
   var mLocations = MutableLiveData<List<Location>>()
   val locations: LiveData<List<Location>>
         get() = mLocations
   override val coroutineContext: CoroutineContext
      get() = job + Dispatchers.IO

   fun validateEmail(userEmail: String) {
      mEmail.value = userEmail
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
}