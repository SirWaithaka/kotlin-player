package com.example.tvnavigation.data.db.models


/*
 * This device model abstracts the functions for mutating
 * and getting the values for the device. Devices are fetched
 * from the device_view <DatabaseView> (READ ONLY) and mutated
 * through the device table.
 *
 * TODO("Find a way to incorporate into repository and/or DAO")
 */

import com.example.tvnavigation.data.db.DeviceDao
import com.example.tvnavigation.data.db.entities.Device
import kotlinx.coroutines.*
import java.time.ZonedDateTime
import kotlin.coroutines.CoroutineContext

class DeviceModel(private val deviceDao: DeviceDao): CoroutineScope {

//   private val Tag = "DeviceModel"
   private val job = Job()
   override val coroutineContext: CoroutineContext
      get() = job + Dispatchers.IO

   init {
      GlobalScope.launch {
         initialize()
      }
   }

   var authStatus: Boolean
      get() = runBlocking(coroutineContext) {
         withContext(this.coroutineContext) {
            return@withContext deviceDao.get().authStatus
         }
      }
      set(value) = runBlocking(coroutineContext) {
         withContext(this.coroutineContext) {
            deviceDao.updateAuthStatus(value)
            return@withContext
         }
      }


   var authToken: String
      get() = runBlocking(coroutineContext) {
         withContext(this.coroutineContext) {
            return@withContext deviceDao.get().authToken
         }
      }
      set(value) = runBlocking(coroutineContext) {
         withContext(this.coroutineContext) {
            deviceDao.updateAuthToken(value)
            return@withContext
         }
      }


   var locationId: String
      get() = runBlocking(coroutineContext) {
         withContext(this.coroutineContext) {
            return@withContext deviceDao.get().locationId
         }
      }
      set(value) = runBlocking(coroutineContext) {
         withContext(this.coroutineContext) {
            deviceDao.updateLocationID(value)
            return@withContext
         }
      }


   var locationName: String
      get() = runBlocking(coroutineContext) {
         withContext(this.coroutineContext) {
            return@withContext deviceDao.get().locationName
         }
      }
      set(value) = runBlocking(coroutineContext) {
         withContext(this.coroutineContext) {
            deviceDao.updateLocationName(value)
            return@withContext
         }
      }


   var locationEmail: String
      get() = runBlocking(coroutineContext) {
         withContext(this.coroutineContext) {
            return@withContext deviceDao.get().locationEmail
         }
      }
      set(value) = runBlocking(coroutineContext) {
         withContext(this.coroutineContext) {
            deviceDao.updateLocationEmail(value)
            return@withContext
         }
      }


   // get and set playerId
   var playerId: String
      get() = runBlocking(coroutineContext) {
         withContext(this.coroutineContext) {
            return@withContext deviceDao.get().playerId
         }
      }
      set(value) = runBlocking(coroutineContext) {
         withContext(this.coroutineContext) {
            deviceDao.updatePlayerID(value)
            return@withContext
         }
      }


   var serialNumber: String
      get() = runBlocking(coroutineContext) {
         withContext(this.coroutineContext) {
            return@withContext deviceDao.get().serialNumber
         }
      }
      set(value) = runBlocking(coroutineContext) {
         withContext(this.coroutineContext) {
            deviceDao.updateSerialNumber(value)
            return@withContext
         }
      }


   var lastDownloadDate: ZonedDateTime?
      get() = runBlocking(coroutineContext) {
         withContext(this.coroutineContext) {
            return@withContext deviceDao.get().lastDownloadDate
         }
      }
      set(value) = runBlocking(coroutineContext) {
         withContext(this.coroutineContext) {
            deviceDao.updateLastDownloadDate(value!!)
            return@withContext
         }
      }


   suspend fun clear() {
      withContext(coroutineContext) {
         deviceDao.delete()
         deviceDao.init(
            Device(locationEmail = "", serialNumber = "", authToken = "")
         )
      }
   }

   suspend fun retrieve(): Device {
      return withContext(coroutineContext) {
         return@withContext deviceDao.get()
      }
   }

   suspend fun update(device: Device) {
      withContext(coroutineContext) {
         deviceDao.upsert(device)
      }
   }

   private suspend fun initialize() {
      withContext(coroutineContext) {
         val device: Device? = deviceDao.get()
         if (device == null)
            deviceDao.init(
               Device(locationEmail = "", serialNumber = "", authToken = "")
            )
      }
   }
}