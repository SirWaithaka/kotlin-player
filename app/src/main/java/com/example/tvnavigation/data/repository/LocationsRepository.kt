package com.example.tvnavigation.data.repository

import com.example.tvnavigation.data.db.entities.Location

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