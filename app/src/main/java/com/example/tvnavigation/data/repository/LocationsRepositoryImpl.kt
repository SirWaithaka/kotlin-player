package com.example.tvnavigation.data.repository

/**
 * In this Repository we handle fetching of data and persisting it on the db
 * It implements the DAOs and the DataSource handlers.
 */

import androidx.lifecycle.LiveData
import com.example.tvnavigation.data.db.LocationDao
import com.example.tvnavigation.data.db.entities.Location
import com.example.tvnavigation.data.repository.datasources.LocationsDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.ZonedDateTime

class LocationsRepositoryImpl(
      private val locationDao: LocationDao,
      private val locationsDataSource: LocationsDataSource
) : LocationsRepository {

   init {
      locationsDataSource.downloadedLocations.observeForever {
         persistFetchedLocationsList(it.locations)
      }
   }

   override suspend fun getLocations(): LiveData<List<Location>> {
      return withContext(Dispatchers.IO) {
         initLocations()
         return@withContext locationDao.getAllLocations()
      }
   }

   private fun persistFetchedLocationsList(fetchedLocationsList: List<Location>) {
      GlobalScope.launch(Dispatchers.IO) {
         locationDao.insertLocations(fetchedLocationsList)
      }
   }

   private suspend fun initLocations() {
//      if (isFetchLocationsNeeded(ZonedDateTime.now().minusHours(1)))
         this.fetchLocations("kennwaithaka@gmail.com")
   }

   private suspend fun fetchLocations(email: String) {
      locationsDataSource.fetchLocations(email)
   }

   private fun isFetchLocationsNeeded(lastTimeFetched: ZonedDateTime): Boolean {
      val thirtyMinutesAgo = ZonedDateTime.now().minusMinutes(30)
      return lastTimeFetched.isBefore(thirtyMinutesAgo)
   }
}