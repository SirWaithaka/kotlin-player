package com.example.tvnavigation.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.tvnavigation.data.repository.LocationsRepository
import com.example.tvnavigation.internal.lazyDeferred

class LocationsViewModel(
      private val locationsRepository: LocationsRepository
) : ViewModel() {

   val locations by lazyDeferred { locationsRepository.getLocations() }

}