package com.example.tvnavigation.data.network.responses

import com.example.tvnavigation.data.interfaces.AdvertInterface

class AdvertisementResponse(
   override val adId: String,
   override val adName: String,
   override val adRevoked: Boolean,
   override val id: String,
   override val timeOfDay: String
): AdvertInterface