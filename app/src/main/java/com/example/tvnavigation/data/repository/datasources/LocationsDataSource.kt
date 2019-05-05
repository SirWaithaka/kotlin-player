package com.example.tvnavigation.data.repository.datasources

import androidx.lifecycle.LiveData
import com.example.tvnavigation.data.network.responses.LocationsResponse
import com.example.tvnavigation.data.network.responses.LoginResponse

interface LocationsDataSource {
   val downloadedLocations: LiveData<LocationsResponse>
   val httpErrorResponse: LiveData<String>
   val loginResponse: LiveData<LoginResponse>
   val isAuthenticated: LiveData<Boolean>

   suspend fun fetchLocations(email: String)
   suspend fun authenticate(id: String, serialNumber: String, password: String)
}