package com.example.tvnavigation.data.network.response

import com.example.tvnavigation.data.db.entities.Location

data class LocationsResponse(
   val locations: List<Location>
)