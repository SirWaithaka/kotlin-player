package com.example.tvnavigation.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tvnavigation.data.db.entities.Advert


@Dao
interface AdvertDao {

   @Query("select * from adverts")
   fun retrieveAdverts(): List<Advert>

   // operations on the adverts table
   @Insert(onConflict = OnConflictStrategy.IGNORE)
   fun upsertAdverts(adverts: List<Advert>)
}