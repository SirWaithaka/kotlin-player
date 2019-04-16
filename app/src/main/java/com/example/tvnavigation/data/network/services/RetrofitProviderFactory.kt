package com.example.tvnavigation.data.network.services

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitProviderFactory {

   private var retrofit: Retrofit? = null
   private const val BASE_URL = "https://youtise-location-dev.herokuapp.com/api/"
   fun instance(): Retrofit {

      if (retrofit == null) {
         retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
      }
      return retrofit!!
   }
}