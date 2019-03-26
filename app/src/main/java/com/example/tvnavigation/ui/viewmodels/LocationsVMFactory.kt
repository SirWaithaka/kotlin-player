package com.example.tvnavigation.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tvnavigation.data.repository.LocationsRepository

class LocationsVMFactory(
      private val locationsRepository: LocationsRepository
) : ViewModelProvider.NewInstanceFactory() {

   @Suppress("UNCHECKED_CAST")
   override fun <T : ViewModel?> create(modelClass: Class<T>): T {
      return LocationsViewModel(locationsRepository) as T
   }
}