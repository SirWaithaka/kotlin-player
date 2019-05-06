package com.example.player.data.network.responses

import com.example.player.data.db.entities.Advert

data class AdvertisementsResponse(
   val adverts: List<Advert>
)