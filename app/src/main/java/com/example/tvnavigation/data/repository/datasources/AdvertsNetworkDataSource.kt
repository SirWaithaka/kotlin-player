package com.example.tvnavigation.data.repository.datasources

import androidx.lifecycle.LiveData
import com.example.tvnavigation.data.network.responses.AdvertisementsResponse

interface AdvertsNetworkDataSource {
   val downloadedAdverts: LiveData<AdvertisementsResponse>
   val httpErrorResponse: LiveData<String>

   suspend fun fetchCurrentAds()
   suspend fun downloadMedia()
}