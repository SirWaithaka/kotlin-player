package com.example.tvnavigation.data.network.responses

import com.example.tvnavigation.data.interfaces.LocationInterface

data class LocationResponse(
   override val id: String,
   override val placeName: String,
   override val placePassword: String
): LocationInterface