package com.example.player.data.repository

/**
 * In this Repository we handle fetching of data and persisting it on the db
 * It implements the DAOs and the DataSource handlers.
 */

import android.util.Log
import com.example.player.data.db.LocationDao
import com.example.player.data.db.entities.Location
import com.example.player.data.repository.datasources.LocationsDataSource
import com.example.player.internal.CURRENT_TIME
import com.example.player.internal.getDeviceSerialNumber
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.ZonedDateTime


class LocationsRepositoryImpl(
      private val locationDao: LocationDao,
      private val locationsDataSource: LocationsDataSource
) : LocationsRepository {

   private val Tag = "LocationsRepository"
   private var listener: LocationsRepository.LocationsFetchedListener? = null
   private val serialNumber by lazy { getDeviceSerialNumber() }

   /**
    * Described is the process of getting data from the api and storing it to db
    *
    * On instance initialization
    *    1. Call the network datasource and observe for new data
    *       - If null do nothing
    *       - when data is updated persist it to local db
    *    2. When user has called the @retrieveLocations method,
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

   override suspend fun authenticate(id: String, password: String) {
      Log.d(Tag, "serial: $serialNumber")
      locationsDataSource.authenticate(id, serialNumber, password)
   }

   override suspend fun retrieveLocations(): List<Location> {
      return withContext(Dispatchers.IO) {
         return@withContext locationDao.getAllLocations()
      }
   }

   override suspend fun validateEmail(userEmail: String) {
      fetchLocations(userEmail)
   }

   override suspend fun getLocations(email: String) {
      Log.d(Tag, "Getting Locations")

      // if cache has expired -> fetch locations
      if (hasCacheExpired(CURRENT_TIME.minusHours(1)))
         return this.fetchLocations(email)

      // if no locations in local db -> fetch locations
      val retrievedLocations = retrieveLocations()
      if (retrievedLocations.isNotEmpty())
         listener?.onLocationsFetched(retrievedLocations)
      else
         this.fetchLocations(email)
   }

   private fun persistFetchedLocationsList(fetchedLocationsList: List<Location>) {
      GlobalScope.launch(Dispatchers.IO) {
         locationDao.insertLocations(fetchedLocationsList)
      }
   }

   /*
    * Fetch locations through api service
    */
   private suspend fun fetchLocations(email: String) {
      locationsDataSource.fetchLocations(email)
   }

   /*
    * Function to check if cache is more than 30MINUTES old
    */
   private fun hasCacheExpired(lastTimeFetched: ZonedDateTime): Boolean {
      val thirtyMinutesAgo = CURRENT_TIME.minusMinutes(30)
      return lastTimeFetched.isBefore(thirtyMinutesAgo)
   }

   override fun setOnLocationsFetchedListener(listener: LocationsRepository.LocationsFetchedListener) {
      this.listener = listener
   }
}