package com.example.player.data.network.responses

import com.example.player.data.interfaces.LocationInterface

data class LocationResponse(
   override val id: String,
   override val placeName: String,
   override val placePassword: String
): LocationInterface