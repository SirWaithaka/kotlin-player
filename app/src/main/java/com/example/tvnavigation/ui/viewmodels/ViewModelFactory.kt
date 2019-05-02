package com.example.tvnavigation.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tvnavigation.data.network.ErrorsHandler
import com.example.tvnavigation.data.repository.AdvertsRepository
import com.example.tvnavigation.data.repository.DeviceRepository
import com.example.tvnavigation.data.repository.LocationsRepository
import java.lang.IllegalArgumentException

class ViewModelFactory(
      private val locationsRepository: LocationsRepository,
      private val advertsRepository: AdvertsRepository,
      private val deviceRepository: DeviceRepository,
      private val errorsHandler: ErrorsHandler
) : ViewModelProvider.NewInstanceFactory() {

   @Suppress("UNCHECKED_CAST")
   override fun <T : ViewModel?> create(modelClass: Class<T>): T {
      return when (modelClass) {
         LocationsViewModel::class.java -> LocationsViewModel(locationsRepository, deviceRepository) as T
         ErrorsViewModel::class.java -> ErrorsViewModel(errorsHandler) as T
         PlayerViewModel::class.java -> PlayerViewModel(advertsRepository) as T
         AdvertsViewModel::class.java -> AdvertsViewModel(advertsRepository, deviceRepository) as T
         SettingsViewModel::class.java -> SettingsViewModel(advertsRepository, deviceRepository) as T
         else -> throw IllegalArgumentException("Unknown ViewModel: ${modelClass.simpleName}")
      }
   }
}