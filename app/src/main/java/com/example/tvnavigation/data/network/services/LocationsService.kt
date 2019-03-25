package com.example.tvnavigation.data.network.services

import com.example.tvnavigation.data.network.ConnectivityInterceptor
import com.example.tvnavigation.data.network.responses.LocationsResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface LocationsService {

   // Example - /api/location/kennwaithaka@gmail.com
   @GET("location/{email}")
   fun getLocationsByEmail(
      @Path("email") email : String
   ): Deferred<LocationsResponse>

   companion object {
      operator fun invoke(connectivityInterceptor: ConnectivityInterceptor): LocationsService {
         val okHttpClient = OkHttpClient.Builder()
               .addInterceptor(connectivityInterceptor)
               .build()

         return Retrofit.Builder()
               .client(okHttpClient)
               .baseUrl("https://youtise-location-dev.herokuapp.com/api/")
               .addCallAdapterFactory(CoroutineCallAdapterFactory())
               .addConverterFactory(GsonConverterFactory.create())
               .build()
               .create(LocationsService::class.java)
      }
   }
}