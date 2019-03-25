package com.example.tvnavigation.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "locations")
data class Location (
   @ColumnInfo(name = "place_id") val id: String,
   val placeName: String,
   val placePassword: String
) {

   @PrimaryKey()
   val uid: Int = 0
}