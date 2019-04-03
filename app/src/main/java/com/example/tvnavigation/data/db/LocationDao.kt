package com.example.tvnavigation.data.db

import androidx.room.*
import com.example.tvnavigation.data.db.entities.Advert
import com.example.tvnavigation.data.db.entities.Device
import com.example.tvnavigation.data.db.entities.Location

/**
 * Defines the data access functions for the locations entity
 * Entails the get and update functions for the locations tuples
 *
 * Will handle db logic for all tables in the app
 */
@Dao
interface LocationDao {
   // updates and inserts data
   @Insert(onConflict = OnConflictStrategy.REPLACE)
   fun insertLocations(locations: List<Location>)

   @Query("select * from locations where place_id = :id")
   fun getLocation(id: String): Location

   @Query("select * from locations")
   fun getAllLocations(): List<Location>

   // Operations on the Device table
   @Query("select * from device")
   fun getDeviceInfo(): Device

   // upsert fn - Update and Insert
   @Insert(onConflict = OnConflictStrategy.REPLACE)
   fun upsertDeviceInfo(updatedDevice: Device)
}