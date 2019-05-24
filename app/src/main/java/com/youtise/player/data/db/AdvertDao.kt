package com.example.player.data.db

import androidx.room.*
import com.example.player.data.db.entities.Advert

/**
 * Operations on the adverts table
 */
@Dao
interface AdvertDao {

   @Query("select * from adverts")
   fun retrieveAdverts(): List<Advert>

   @Insert(onConflict = OnConflictStrategy.IGNORE)
   fun upsertAdverts(adverts: List<Advert>): List<Long>

   @Query("DELETE FROM adverts")
   fun deleteAdverts(): Int
}