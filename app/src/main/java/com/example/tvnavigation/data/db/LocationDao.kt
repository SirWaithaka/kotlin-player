package com.example.tvnavigation.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tvnavigation.data.db.entities.Device
import com.example.tvnavigation.data.db.entities.Location

/**
 * Defines the data access functions for the locations entity
 * Entails the get and update functions for the locations tuples
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

   @Query("select * from device where registeredEmail = :email")
   fun getRegisteredEmail(email: String): Device

   @Insert(onConflict = OnConflictStrategy.REPLACE)
   fun insertDeviceInfo(info: Device)
}