package com.example.player.data.repository

import com.example.player.data.db.entities.Location

interface LocationsRepository {
   suspend fun getLocations(): List<Location>
   suspend fun authenticate(id: String, password: String)
   suspend fun getLocationsByEmail(email: String)

   fun getAuthenticationStatus(): Boolean

   fun setOnLocationsFetchedListener(listener: LocationsFetchedListener)
   interface LocationsFetchedListener {
      fun onLocationsFetched(locations: List<Location>)
   }
}