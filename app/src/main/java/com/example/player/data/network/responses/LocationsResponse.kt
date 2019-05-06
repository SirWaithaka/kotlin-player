package com.example.player.data.network.responses

import com.example.player.data.db.entities.Location

data class LocationsResponse(
   val locations: List<Location>
)