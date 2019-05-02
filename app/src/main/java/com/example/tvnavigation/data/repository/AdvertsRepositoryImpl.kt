package com.example.tvnavigation.data.repository

import androidx.lifecycle.LiveData
import com.example.tvnavigation.data.db.AdvertDao
import com.example.tvnavigation.data.db.entities.Advert
import com.example.tvnavigation.data.network.AdvertLog
import com.example.tvnavigation.data.repository.datasources.AdvertsNetworkDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AdvertsRepositoryImpl(
      private val advertDao: AdvertDao,
      private val advertsNetworkDataSource: AdvertsNetworkDataSource
) : AdvertsRepository {

//   private val TAG = "AdvertsRepository"
   private var listener: AdvertsRepository.AdvertsFetchedListener? = null
   private var retrievedAdverts = listOf<Advert>()

   init {
       advertsNetworkDataSource.downloadedAdverts.observeForever {
          this.listener?.onAdvertsFetched(it.adverts)
          persistFetchedAdverts(it.adverts)
       }
   }

   override suspend fun postAdvertLog(log: AdvertLog) {
      advertsNetworkDataSource.postAdvertLog(log)
   }

   override suspend fun invokePopCapture(advertId: String) {
      advertsNetworkDataSource.invokePopCapture(advertId)
   }

   override suspend fun retrieveAdverts(): List<Advert> {
      return withContext(Dispatchers.IO) {
         return@withContext if (retrievedAdverts.isNotEmpty()) retrievedAdverts
         else {
            retrievedAdverts = advertDao.retrieveAdverts()
            retrievedAdverts
         }
      }
   }

   override suspend fun updateAdverts(adverts: List<Advert>) {
      withContext(Dispatchers.IO) {
         advertDao.upsertAdverts(adverts)
      }
   }

   override suspend fun resetAdverts() =
      advertDao.deleteAdverts()

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
            advertDao.upsertAdverts(fetchedAdverts)
            this@AdvertsRepositoryImpl.listener?.onAdvertsPersisted(true)
         }
      }
   }

   override fun setAdvertsFetchedListener(listener: AdvertsRepository.AdvertsFetchedListener) {
      this.listener = listener
   }
}