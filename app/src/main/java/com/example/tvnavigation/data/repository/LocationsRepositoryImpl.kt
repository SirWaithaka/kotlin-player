package com.example.tvnavigation.data.repository

/**
 * In this Repository we handle fetching of data and persisting it on the db
 * It implements the DAOs and the DataSource handlers.
 */

import android.os.Build
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

//   private val TAG = "LocationRepository"
   private lateinit var userEmail: String
   private var isAuthenticated: Boolean = false
   private var listener: LocationsRepository.LocationsFetchedListener? = null
   private val serialNumber by lazy {
      try {
         return@lazy Build.getSerial()
      } catch (e: SecurityException) {
         return@lazy ""
      }
   }

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
         this.listener?.onLocationsFetched(it.locations)
         persistFetchedLocationsList(it.locations)
      }
   }

   override fun getAuthenticationStatus(): Boolean {
      return isAuthenticated
   }

   override suspend fun getLocations(): List<Location> {
      return withContext(Dispatchers.IO) {
         return@withContext locationDao.getAllLocations()
      }
   }

   override suspend fun getLocationsByEmail(email: String) {
      this.userEmail = email
      val retrievedLocations = getLocations()
      if (retrievedLocations.isNotEmpty())
         listener?.onLocationsFetched(retrievedLocations)
      else {
         initLocations()
      }
   }

   override suspend fun authenticate(id: String, password: String) {
      locationsDataSource.authenticate(id, serialNumber, password)
   }

   private fun persistFetchedLocationsList(fetchedLocationsList: List<Location>) {
      GlobalScope.launch(Dispatchers.IO) {
         locationDao.insertLocations(fetchedLocationsList)
      }
   }

   private suspend fun initLocations() {
      if (isFetchLocationsNeeded(ZonedDateTime.now().minusHours(1)))
         this.fetchLocations(userEmail)
   }

   private suspend fun fetchLocations(email: String) {
      locationsDataSource.fetchLocations(email)
   }

   private fun isFetchLocationsNeeded(lastTimeFetched: ZonedDateTime): Boolean {
      val thirtyMinutesAgo = ZonedDateTime.now().minusMinutes(30)
      return lastTimeFetched.isBefore(thirtyMinutesAgo)
   }

   override fun setOnLocationsFetchedListener(listener: LocationsRepository.LocationsFetchedListener) {
      this.listener = listener
   }
}