package com.youtise.player.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.youtise.player.data.interfaces.LocationInterface


@Entity(tableName = "locations")
data class Location (

   @PrimaryKey
   @ColumnInfo(name = "place_id")
   override val id: String,
   override val placeName: String,
   override val placePassword: String
): LocationInterface {


   override fun toString(): String {
      return placeName
   }
}