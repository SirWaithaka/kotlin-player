package com.example.tvnavigation.data.repository.datasources

import androidx.lifecycle.LiveData
import com.example.tvnavigation.data.network.responses.AdvertisementsResponse

interface AdvertsNetworkDataSource {
   val downloadedLocations: LiveData<AdvertisementsResponse>
   val httpErrorResponse: LiveData<String>

   suspend fun fetchCurrentAds()
}