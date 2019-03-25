package com.example.tvnavigation.data.repositories

import androidx.lifecycle.LiveData
import com.example.tvnavigation.data.network.responses.LocationsResponse

interface LocationsDataSource {
   val downloadedLocations: LiveData<LocationsResponse>

   suspend fun fetchLocations(email: String)
}