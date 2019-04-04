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

   init {
       advertsNetworkDataSource.downloadedAdverts.observeForever {
          Log.d(TAG, "Ads: $it")
          this.listener?.onAdvertsFetched(it.adverts)
          persistFetchedAdverts(it.adverts)
       }
   }

   override suspend fun retrieveAdverts(): List<Advert> {
      return withContext(Dispatchers.IO) {
         return@withContext advertDao.retrieveAdverts()
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

//   suspend fun getAdverts(): List<Advert> {
//      return retrieveAdverts()
//   }

   override suspend fun fetchAdverts() {
      advertsNetworkDataSource.fetchCurrentAds()
   }

   private fun persistFetchedAdverts(fetchedAdverts: List<Advert>) {
      GlobalScope.launch {
         val result = advertDao.upsertAdverts(fetchedAdverts)
         Log.d(TAG, "Upsert result: ${result.toString()}")
         this@AdvertsRepositoryImpl.listener?.onAdvertsPersisted(true)
      }
   }

   override fun setAdvertsFetchedListener(listener: AdvertsRepository.AdvertsFetchedListener) {
      this.listener = listener
   }
}