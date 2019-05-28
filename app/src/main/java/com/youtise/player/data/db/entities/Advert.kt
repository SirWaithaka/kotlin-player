package com.youtise.player.data.db.entities

import androidx.room.*

@Entity(tableName = "adverts")
data class Advert(
   @PrimaryKey
   @ColumnInfo(name = "ad_id")
   val id: String,
   val adName: String,
   val mediaType: String,
   val mediaURL: String,
   val mediaKey: String,
   val thumbnailUrl: String,
   val timeOfDay: List<String>
) {
   val fileName: String
      get() = mediaKey.split("/")[1]
}

class ListToString {

   @TypeConverter
   fun toString(list: List<String>): String {
      return list.joinToString(separator = ":")
   }

   @TypeConverter
   fun toList(string: String): List<String> {
      return string.split(":")
   }
}