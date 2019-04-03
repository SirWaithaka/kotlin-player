package com.example.tvnavigation.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tvnavigation.data.repository.AdvertsRepository
import com.example.tvnavigation.data.repository.LocationsRepository

class ViewModelFactory(
      private val locationsRepository: LocationsRepository,
      private val advertsRepository: AdvertsRepository
) : ViewModelProvider.NewInstanceFactory() {

   @Suppress("UNCHECKED_CAST")
   override fun <T : ViewModel?> create(modelClass: Class<T>): T {
      return if (modelClass == LocationsViewModel::class.java) LocationsViewModel(locationsRepository) as T
      else AdvertsViewModel(advertsRepository) as T
   }
}