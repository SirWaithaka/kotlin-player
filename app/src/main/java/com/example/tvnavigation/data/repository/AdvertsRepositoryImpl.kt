package com.example.tvnavigation.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.tvnavigation.data.db.AdvertDao
import com.example.tvnavigation.data.db.entities.Advert
import com.example.tvnavigation.data.repository.datasources.AdvertsNetworkDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AdvertsRepositoryImpl(
      private val advertDao: AdvertDao,
      private val advertsNetworkDataSource: AdvertsNetworkDataSource
) : AdvertsRepository {

   private val TAG = "AdvertsRepository"
   private var listener: AdvertsRepository.AdvertsFetchedListener? = null
   private var retrievedAdverts = listOf<Advert>()

   init {
       advertsNetworkDataSource.downloadedAdverts.observeForever {
          logResponse(it.adverts)
          this.listener?.onAdvertsFetched(it.adverts)
          persistFetchedAdverts(it.adverts)
       }
   }

   private fun logResponse(adverts: List<Advert>) {
      for (advert in adverts) {
         Log.d(TAG, "advert: ${advert.adName}")
      }
   }
   override suspend fun retrieveAdverts(): List<Advert> {
      return withContext(Dispatchers.IO) {
         retrievedAdverts = advertDao.retrieveAdverts()
         return@withContext retrievedAdverts
      }
   }

   override suspend fun updateAdverts(adverts: List<Advert>) {
      withContext(Dispatchers.IO) {
         advertDao.upsertAdverts(adverts)
      }
   }

   override fun getHttpErrorResponses(): LiveData<String> {
      TODO("not implemented")
   }

   override suspend fun fetchAdverts() {
      advertsNetworkDataSource.fetchCurrentAds()
   }

   private fun persistFetchedAdverts(fetchedAdverts: List<Advert>) {
      GlobalScope.launch {
         val toPersist = fetchedAdverts.minus(retrievedAdverts)
         if (toPersist.isNotEmpty()) {
            val result = advertDao.upsertAdverts(fetchedAdverts)
            Log.d(TAG, "Upsert result: $result")
            this@AdvertsRepositoryImpl.listener?.onAdvertsPersisted(true)
         }
      }
   }

   override fun setAdvertsFetchedListener(listener: AdvertsRepository.AdvertsFetchedListener) {
      this.listener = listener
   }
}