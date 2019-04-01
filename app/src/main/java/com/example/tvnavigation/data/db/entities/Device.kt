package com.example.tvnavigation.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

const val REGISTERED_USER_ID = 0

@Entity(tableName = "device")
data class Device (
   val registeredEmail: String,
   val serialNumber: String
) {

   @PrimaryKey(autoGenerate = false)
   var uid: Int = REGISTERED_USER_ID
}