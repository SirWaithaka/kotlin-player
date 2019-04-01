package com.example.tvnavigation.data.repository

/**
 * In this Repository we handle fetching of data and persisting it on the db
 * It implements the DAOs and the DataSource handlers.
 */

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.tvnavigation.data.db.LocationDao
import com.example.tvnavigation.data.db.entities.Advert
import com.example.tvnavigation.data.db.entities.Device
import com.example.tvnavigation.data.db.entities.Location
import com.example.tvnavigation.data.network.interceptors.HttpErrorInterceptor
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
   private var httpErrorResponse: LiveData<String>

   init {
      locationsDataSource.downloadedLocations.observeForever {
         persistFetchedLocationsList(it.locations)
      }
      httpErrorResponse = locationsDataSource.httpErrorResponse
   }

   override fun getHttpErrorResponses(): LiveData<String> {
      return this.httpErrorResponse
   }

   override suspend fun getLocations(email: String): List<Location> {
      this.userEmail = email
      return withContext(Dispatchers.IO) {
         initLocations()
         val device = locationDao.getRegisteredEmail(email)
         if (!device.registeredEmail.equals(email, ignoreCase = false))
            return@withContext listOf<Location>()
         return@withContext locationDao.getAllLocations()
      }
   }

   override fun getCurrentAds(locationId: String, mPassword: String): List<Advert> {

      return listOf<Advert>()
   }

   private fun persistFetchedLocationsList(fetchedLocationsList: List<Location>) {
      GlobalScope.launch(Dispatchers.IO) {
         Log.d(TAG  , "Persist Location called")
         locationDao.insertLocations(fetchedLocationsList)
      }
   }

   private suspend fun initLocations() {
      val device = Device(userEmail, "0")
      locationDao.insertDeviceInfo(device)
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