package com.youtise.player.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.youtise.player.data.db.models.DeviceModel
import com.youtise.player.data.network.ErrorsHandler
import com.youtise.player.data.repository.AdvertsRepository
import com.youtise.player.data.repository.DeviceRepository
import com.youtise.player.data.repository.LocationsRepository
import java.lang.IllegalArgumentException

class ViewModelFactory(
      private val locationsRepository: LocationsRepository,
      private val advertsRepository: AdvertsRepository,
      private val deviceRepository: DeviceRepository,
      private val deviceModel: DeviceModel,
      private val errorsHandler: ErrorsHandler
) : ViewModelProvider.NewInstanceFactory() {

   @Suppress("UNCHECKED_CAST")
   override fun <T : ViewModel?> create(modelClass: Class<T>): T {
      return when (modelClass) {
         LocationsViewModel::class.java -> LocationsViewModel(locationsRepository, deviceModel, deviceRepository) as T
         ErrorsViewModel::class.java -> ErrorsViewModel(errorsHandler) as T
         PlayerViewModel::class.java -> PlayerViewModel(advertsRepository) as T
         AdvertsViewModel::class.java -> AdvertsViewModel(advertsRepository, deviceModel) as T
         SettingsViewModel::class.java -> SettingsViewModel(advertsRepository, deviceModel) as T
         else -> throw IllegalArgumentException("Unknown ViewModel: ${modelClass.simpleName}")
      }
   }
}