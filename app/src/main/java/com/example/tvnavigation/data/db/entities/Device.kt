package com.example.tvnavigation.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.time.ZonedDateTime

const val REGISTERED_USER_ID = 0

@Entity(tableName = "device")
data class Device (
   var locationEmail: String = "",
   var serialNumber: String = "",
   var authToken: String = ""
) {

   @PrimaryKey(autoGenerate = false)
   var uid: Int = REGISTERED_USER_ID
   var authStatus: Boolean = false
   var locationId: String = ""
   var locationName: String = ""
   var playerId: String = ""
   var lastDownloadDate: ZonedDateTime? = null
}

class DateTimeToString {

   @TypeConverter
   fun toString(date: ZonedDateTime?): String {
      if (date != null) return date.toString()
      return ""
   }

   @TypeConverter
   fun toDateTime(string: String): ZonedDateTime? {
      if (string.isNotEmpty())
         return ZonedDateTime.parse(string)
      return null
   }
}