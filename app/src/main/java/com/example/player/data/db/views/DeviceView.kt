package com.example.player.data.db.views

import androidx.room.DatabaseView

/*
 * This table view is an identical representation of the Device table
 * It is created to mitigate around the deadlock error on the Device
 * table where two threads would read and try update the same record
 *
 * TODO("Find a better solution than this around that error")
 */
@DatabaseView(
   value="SELECT * FROM device WHERE uid = 0",
   viewName = "device_view"
)
data class DeviceView (

   val uid: Int,
   val authStatus: Boolean,
   val authToken: String,

   val locationId: String,
   val locationEmail: String,
   val locationName: String,

   val playerId: String,
   val serialNumber: String
)