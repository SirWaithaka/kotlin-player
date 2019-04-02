package com.example.tvnavigation.data.repository

import androidx.lifecycle.LiveData
import com.example.tvnavigation.data.db.entities.Advert
import com.example.tvnavigation.data.db.entities.Location

interface LocationsRepository {
   suspend fun getLocations(email: String): List<Location>
   suspend fun getCurrentAds(): List<Advert>
   suspend fun authenticate(id: String, password: String)

   fun getHttpErrorResponses(): LiveData<String>
   fun getAuthenticationStatus(): Boolean
}