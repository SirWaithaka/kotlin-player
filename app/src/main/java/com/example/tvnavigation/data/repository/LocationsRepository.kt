package com.example.tvnavigation.data.repository

import androidx.lifecycle.LiveData
import com.example.tvnavigation.data.db.entities.Location

interface LocationsRepository {
   suspend fun getLocations(email: String): List<Location>
}