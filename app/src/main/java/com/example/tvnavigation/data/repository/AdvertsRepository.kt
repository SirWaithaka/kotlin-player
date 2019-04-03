package com.example.tvnavigation.data.repository

import androidx.lifecycle.LiveData
import com.example.tvnavigation.data.db.entities.Advert

interface AdvertsRepository {
   suspend fun refreshAdverts(): List<Advert>

   fun getHttpErrorResponses(): LiveData<String>
}
