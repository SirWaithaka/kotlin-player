package com.example.tvnavigation.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tvnavigation.data.db.entities.Device


/**
 * Defines the Data Access functions for the device entity
 */
@Dao
interface DeviceDao {

   // Operations on the Device table
   @Query("select * from device")
   fun getDeviceInfo(): Device

   // upsert fn - Update and Insert
   @Insert(onConflict = OnConflictStrategy.REPLACE)
   fun upsertDeviceInfo(updatedDevice: Device)
}