package com.example.tvnavigation.data.db

import androidx.room.*
import com.example.tvnavigation.data.db.entities.Advert

/**
 * Operations on the adverts table
 */
@Dao
interface AdvertDao {

   @Query("select * from adverts")
   fun retrieveAdverts(): List<Advert>

   @Insert(onConflict = OnConflictStrategy.IGNORE)
   fun upsertAdverts(adverts: List<Advert>): List<Long>

   @Delete
   fun deleteAdverts(adverts: List<Advert>)
}