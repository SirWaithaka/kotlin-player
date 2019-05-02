package com.example.tvnavigation.data.db

import androidx.room.*
import com.example.tvnavigation.data.db.entities.Device


/**
 * Defines the Data Access functions for the device entity
 */
@Dao
interface DeviceDao {

   // Operations on the Device table
   @Query("SELECT * FROM device")
   fun getDeviceInfo(): Device

   // upsert fn - Update and Insert
   @Insert(onConflict = OnConflictStrategy.REPLACE)
   fun upsertDeviceInfo(updatedDevice: Device)

   @Query("DELETE FROM device WHERE uid = 0")
   fun deleteDeviceInfo(): Int
}