package com.example.tvnavigation.data.repository

/**
 * In this Repository we handle fetching of data and persisting it on the db
 * It implements the DAOs and the DataSource handlers.
 */

import android.util.Log
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

   private lateinit var userEmail: String
   private var isAuthenticated: Boolean = false

   /**
    * Described is the process of getting data from the api and storing it to db
    *
    * On instance initialization
    *    1. Call the network datasource and observe for new data
    *       - If null do nothing
    *       - when data is updated persist it to local db
    *    2. When user has called the @getLocations method,
    *       - Calls init that checks if data is stale by more than an hour
    *       which then proceeds if true
    *       - Fetch locations calls the network datasource to refresh the data
    *       which thereafter triggers change and propagates to this repository
    *       that persists the data to db
    */
   init {
      locationsDataSource.downloadedLocations.observeForever {
         persistFetchedLocationsList(it.locations)
      }
   }

   override fun getAuthenticationStatus(): Boolean {
      Log.d(TAG, "Auth Status called with value: $isAuthenticated")
      return isAuthenticated
   }

   /**
    * TODO("Fix bug: Locations get returned empty before
    * @persistFetchedLocations has done its thing")
    */
   override suspend fun getLocations(email: String): List<Location> {
      this.userEmail = email
      return withContext(Dispatchers.IO) {
         initLocations()
         return@withContext locationDao.getAllLocations()
      }
   }

   override suspend fun authenticate(id: String, password: String) {
      Log.d(TAG, "Passed in credentials: $id : $password")
      locationsDataSource.authenticate(id, password)
   }

   private fun persistFetchedLocationsList(fetchedLocationsList: List<Location>) {
      GlobalScope.launch(Dispatchers.IO) {
         Log.d(TAG  , "Persist Location called")
         locationDao.insertLocations(fetchedLocationsList)
      }
   }

   private suspend fun initLocations() {
      if (isFetchLocationsNeeded(ZonedDateTime.now().minusHours(1)))
         this.fetchLocations(userEmail)
   }

   private suspend fun fetchLocations(email: String) {
      Log.d(TAG, "DAO called $email")
      locationsDataSource.fetchLocations(email)
   }

   private fun isFetchLocationsNeeded(lastTimeFetched: ZonedDateTime): Boolean {
      val thirtyMinutesAgo = ZonedDateTime.now().minusMinutes(30)
      return lastTimeFetched.isBefore(thirtyMinutesAgo)
   }

   companion object {
       const val TAG = "LocationRepository"
   }
}