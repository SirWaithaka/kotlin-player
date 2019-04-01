package com.example.tvnavigation.data.repository

import androidx.lifecycle.LiveData
import com.example.tvnavigation.data.db.entities.Location

interface LocationsRepository {
   suspend fun getLocations(email: String): List<Location>
   fun getHttpErrorResponses(): LiveData<String>
   fun getCurrentAds(locationId: String, mPassword: String): List<Any>
}