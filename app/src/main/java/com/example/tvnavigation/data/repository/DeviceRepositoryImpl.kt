package com.example.tvnavigation.data.repository

import android.util.Log
import com.example.tvnavigation.data.db.DeviceDao
import com.example.tvnavigation.data.db.entities.Device
import com.example.tvnavigation.data.repository.datasources.LocationsDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DeviceRepositoryImpl(
      private val deviceDao: DeviceDao,
      locationsDataSource: LocationsDataSource
) : DeviceRepository {

   private val TAG = "DeviceRepository"
   private var listener: DeviceRepository.AuthenticationStatusListener? = null


   init {
      locationsDataSource.authToken.observeForever {
         persistFetchedToken(it)
         Log.d(TAG, "Token: $it")
      }
   }

   private fun persistFetchedToken(authToken: String) {
      GlobalScope.launch(Dispatchers.IO) {
         val device = deviceDao.getDeviceInfo()
         device.authToken = authToken
         device.authStatus = true
         device.initialised = true
         deviceDao.upsertDeviceInfo(device)
         listener?.onStatusChanged(true)
         Log.d(TAG, "Still persisting")
      }
   }

   override suspend fun getDeviceInfo(): Device {
      return withContext(Dispatchers.IO) {
         var device = deviceDao.getDeviceInfo()
         if (device == null) {
            device = Device("", "", "")
            deviceDao.upsertDeviceInfo(device)
            return@withContext deviceDao.getDeviceInfo()
         }
         return@withContext device
      }
   }

   override fun setOnAuthStatusChangedListener(authenticationStatusListener: DeviceRepository.AuthenticationStatusListener) {
      this.listener = authenticationStatusListener
   }
}