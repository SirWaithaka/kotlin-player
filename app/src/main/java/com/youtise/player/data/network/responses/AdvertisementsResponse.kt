package com.youtise.player.data.network.responses

import com.youtise.player.data.db.entities.Advert

data class AdvertisementsResponse(
   val adverts: List<Advert>
)