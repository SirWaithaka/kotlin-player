package com.example.tvnavigation.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.tvnavigation.data.db.entities.Location


/**
 * Defines the data access functions for the locations entity
 * Entails the get and update functions for the locations tuples
 */
@Dao
interface LocationDao {
   // updates and inserts data
   @Insert
   fun insertLocations(locations: List<Location>)

   @Query("select * from locations where uid = :uid")
   fun getLocation(uid: Int): LiveData<Location>

   @Query("select * from locations")
   fun getAllLocations(): LiveData<List<Location>>
}