package com.example.tvnavigation.data.repository.datasources

import androidx.lifecycle.LiveData
import com.example.tvnavigation.data.network.responses.LocationsResponse

interface LocationsDataSource {
   val downloadedLocations: LiveData<LocationsResponse>
   val httpErrorResponse: LiveData<String>
   val authToken: LiveData<String>

   suspend fun fetchLocations(email: String)
   suspend fun authenticate(id: String, password: String)
}