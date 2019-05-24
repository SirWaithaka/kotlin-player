package com.example.player.data.db

import androidx.room.*
import com.example.player.data.db.entities.Device
import org.threeten.bp.ZonedDateTime


/**
 * Defines the Data Access functions for the device entity
 */
@Dao
abstract class DeviceDao {

   /*
    * Function that initializes the device db first time app is installed
    */
   @Insert(onConflict = OnConflictStrategy.REPLACE)
   abstract fun init(device: Device)


   /*
    * Return the device information
    */
   @Query("SELECT * FROM device_view")
   abstract fun get(): Device


   /*
    * Updates a single column using custom query
    *
    * TODO("Find a better way of updating the columns")
    */
   @Query("UPDATE device SET authStatus = :value")
   abstract fun updateAuthStatus(value: Boolean)

   @Query("UPDATE device SET authToken = :value")
   abstract fun updateAuthToken(value: String)

   @Query("UPDATE device SET locationId = :value")
   abstract fun updateLocationID(value: String)

   @Query("UPDATE device SET locationName = :value")
   abstract fun updateLocationName(value: String)

   @Query("UPDATE device SET locationEmail = :value")
   abstract fun updateLocationEmail(value: String)

   @Query("UPDATE device SET playerId = :value")
   abstract fun updatePlayerID(value: String)

   @Query("UPDATE device SET serialNumber = :value")
   abstract fun updateSerialNumber(value: String)

   @Query("UPDATE device SET lastDownloadDate = :value")
   abstract fun updateLastDownloadDate(value: ZonedDateTime)


   /*
    * Update all columns with new data
    */
   @Insert(onConflict = OnConflictStrategy.REPLACE)
   abstract fun upsert(device: Device)


   /*
    * Delete tuple
    */
   @Query("DELETE FROM device WHERE uid = 0")
   abstract fun delete()
}