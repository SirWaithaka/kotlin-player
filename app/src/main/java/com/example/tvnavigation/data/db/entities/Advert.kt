package com.example.tvnavigation.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.tvnavigation.data.interfaces.AdvertInterface

@Entity(tableName = "adverts")
data class Advert(
   @PrimaryKey
   @ColumnInfo(name = "ad_id")
   override val adId: String,
   override val adName: String,
   override val adRevoked: Boolean,
   @ColumnInfo(name = "slot_id")
   override val id: String,
   override val timeOfDay: String
): AdvertInterface