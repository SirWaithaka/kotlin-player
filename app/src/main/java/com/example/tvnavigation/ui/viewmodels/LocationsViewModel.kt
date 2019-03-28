package com.example.tvnavigation.ui.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tvnavigation.data.repository.LocationsRepository
import com.example.tvnavigation.internal.lazyDeferred

class LocationsViewModel(
      private val locationsRepository: LocationsRepository
) : ViewModel() {

   val mEmail = MutableLiveData<String>()
   val locations by lazyDeferred { locationsRepository.getLocations() }

   fun validateEmail(userEmail: String) {
      mEmail.value = userEmail

      Log.d("ViewModel", "inserted email: $userEmail")
   }
}