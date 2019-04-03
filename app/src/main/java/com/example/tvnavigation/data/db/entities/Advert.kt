package com.example.tvnavigation.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "adverts")
data class Advert(
   @PrimaryKey
   @ColumnInfo(name = "ad_id")
   val adId: String,
   val adName: String,
   val adRevoked: Boolean,
   @ColumnInfo(name = "slot_id")
   val id: String,
   val timeOfDay: String
)