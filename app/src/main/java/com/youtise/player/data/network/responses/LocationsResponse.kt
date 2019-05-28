package com.youtise.player.data.network.responses

import com.youtise.player.data.db.entities.Location

data class LocationsResponse(
   val locations: List<Location>
)