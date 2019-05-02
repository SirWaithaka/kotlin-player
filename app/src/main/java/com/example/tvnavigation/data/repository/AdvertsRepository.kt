package com.example.tvnavigation.data.repository

import androidx.lifecycle.LiveData
import com.example.tvnavigation.data.db.entities.Advert
import com.example.tvnavigation.data.network.AdvertLog

interface AdvertsRepository {
   suspend fun fetchAdverts()
   suspend fun retrieveAdverts(): List<Advert>
   suspend fun updateAdverts(adverts: List<Advert>)
   suspend fun postAdvertLog(log: AdvertLog)
   suspend fun invokePopCapture(advertId: String)
   suspend fun resetAdverts(): Int

   fun getHttpErrorResponses(): LiveData<String>

   fun setAdvertsFetchedListener(listener: AdvertsFetchedListener)
   interface AdvertsFetchedListener {
      fun onAdvertsFetched(adverts: List<Advert>)
      fun onAdvertsPersisted(status: Boolean)
   }
}
