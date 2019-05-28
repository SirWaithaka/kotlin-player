package com.youtise.player.data.network

data class AdvertLog(
   val start: String,
   val end: String,
   val id: String,
   val result: String
) {
   var duration = "1.00"
}