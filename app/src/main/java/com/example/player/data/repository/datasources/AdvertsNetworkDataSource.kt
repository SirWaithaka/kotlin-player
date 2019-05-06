package com.example.player.data.repository.datasources

import androidx.lifecycle.LiveData
import com.example.player.data.network.AdvertLog
import com.example.player.data.network.responses.AdvertisementsResponse

interface AdvertsNetworkDataSource {
   val downloadedAdverts: LiveData<AdvertisementsResponse>
   val httpErrorResponse: LiveData<String>

   suspend fun fetchCurrentAds()
   suspend fun postAdvertLog(log: AdvertLog)
   suspend fun invokePopCapture(advertId: String)
}