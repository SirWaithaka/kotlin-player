package com.example.tvnavigation.data.repository

import android.os.Build
import com.example.tvnavigation.data.db.DeviceDao
import com.example.tvnavigation.data.db.entities.Device
import com.example.tvnavigation.data.repository.datasources.LocationsDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.ZonedDateTime

class DeviceRepositoryImpl(
      private val deviceDao: DeviceDao,
      locationsDataSource: LocationsDataSource
) : DeviceRepository {

//   private val TAG = "DeviceRepository"
   private lateinit var device: Device
   private var locationId: String = ""
   private var listener: DeviceRepository.AuthenticationStatusListener? = null
   private val serialNumber by lazy {
      try {
         return@lazy Build.getSerial()
      } catch (e: SecurityException) {
         return@lazy ""
      }
   }

   init {
      locationsDataSource.authToken.observeForever {
         persistFetchedToken(it)
      }
   }

   private fun persistFetchedToken(authToken: String) {
      GlobalScope.launch(Dispatchers.IO) {
//         val device = deviceDao.getDeviceInfo()
         device.authToken = authToken
         device.authStatus = true
         device.initialised = true
         device.locationId = locationId
         device.serialNumber = serialNumber
         deviceDao.upsertDeviceInfo(device)
         listener?.onStatusChanged(true)
      }
   }

   override fun setLocationId(id: String) {
      locationId = id
   }
   override fun setLastUpdated(date: ZonedDateTime) {
      GlobalScope.launch(Dispatchers.IO) {
         device.lastUpdated = date
         deviceDao.upsertDeviceInfo(device)
      }
   }

   override suspend fun resetDevice() =
      withContext(Dispatchers.IO) {
         return@withContext deviceDao.deleteDeviceInfo()
      }

   override suspend fun getDeviceInfo(): Device {
      return withContext(Dispatchers.IO) {
         // Get Device info
         // On initial startup, could be null
         var init: Device?  = deviceDao.getDeviceInfo()
         if (init == null) {
            init = Device("", serialNumber, "")
            deviceDao.upsertDeviceInfo(init)
            device = deviceDao.getDeviceInfo()
            return@withContext device
         }

         device = init
         return@withContext device
      }
   }

   override fun setOnAuthStatusChangedListener(authenticationStatusListener: DeviceRepository.AuthenticationStatusListener) {
      this.listener = authenticationStatusListener
   }
}
