package com.example.tvnavigation.data.network.responses

import com.example.tvnavigation.data.db.entities.Advert

data class AdvertisementsResponse(
   val adverts: List<Advert>
)