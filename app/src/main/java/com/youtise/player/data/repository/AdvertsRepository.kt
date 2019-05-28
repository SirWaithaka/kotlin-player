package com.youtise.player.data.repository

import androidx.lifecycle.LiveData
import com.youtise.player.data.db.entities.Advert
import com.youtise.player.data.network.AdvertLog

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
