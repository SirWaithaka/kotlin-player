package com.youtise.player.data.db

import androidx.room.*
import com.youtise.player.data.db.entities.Location

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

}