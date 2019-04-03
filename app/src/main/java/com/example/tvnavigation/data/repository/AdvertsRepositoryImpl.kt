package com.example.tvnavigation.data.repository

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

   init {
       advertsNetworkDataSource.downloadedAdverts.observeForever {
          persistFetchedAdverts(it.adverts)
       }
   }

   override suspend fun refreshAdverts(): List<Advert> {
      return withContext(Dispatchers.IO) {
         return@withContext advertDao.retrieveAdverts()
      }
   }

   override fun getHttpErrorResponses(): LiveData<String> {
      TODO("not implemented")
   }

   suspend fun getAdverts(): List<Advert> {
      return refreshAdverts()
   }

   private suspend fun fetchAdverts() {
      advertsNetworkDataSource.fetchCurrentAds()
   }

   private fun persistFetchedAdverts(fetchedAdverts: List<Advert>) {
      GlobalScope.launch {
         advertDao.upsertAdverts(fetchedAdverts)
      }
   }
}