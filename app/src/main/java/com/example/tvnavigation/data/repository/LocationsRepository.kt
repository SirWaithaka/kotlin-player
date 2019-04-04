package com.example.tvnavigation.data.repository

import com.example.tvnavigation.data.db.entities.Location

interface LocationsRepository {
   suspend fun getLocations(email: String): List<Location>
   suspend fun authenticate(id: String, password: String)

   fun getAuthenticationStatus(): Boolean
}