package com.example.tvnavigation.data.network.responses

import com.example.tvnavigation.data.db.entities.Location

data class LocationsResponse(
   val locations: List<Location>
)