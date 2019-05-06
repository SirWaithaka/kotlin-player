package com.example.player.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.example.player.internal.DEVICE_UID
import java.time.ZonedDateTime


@Entity(tableName = "device")
data class Device (
   var locationEmail: String = "",
   var serialNumber: String = "",
   var authToken: String = ""
) {

   @PrimaryKey(autoGenerate = false)
   var uid: Int = DEVICE_UID
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