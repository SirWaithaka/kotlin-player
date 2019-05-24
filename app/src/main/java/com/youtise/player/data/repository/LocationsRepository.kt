package com.example.player.data.repository

import com.example.player.data.db.entities.Location

interface LocationsRepository {
   suspend fun retrieveLocations(): List<Location>
   suspend fun authenticate(id: String, password: String)
   suspend fun getLocations(email: String)
   suspend fun validateEmail(userEmail: String)

   fun setOnLocationsFetchedListener(listener: LocationsFetchedListener)

   interface LocationsFetchedListener {
      fun onLocationsFetched(locations: List<Location>)
   }
}