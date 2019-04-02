package com.example.tvnavigation.data.network.services

import com.example.tvnavigation.data.network.interceptors.HttpErrorInterceptor
import com.example.tvnavigation.data.network.interceptors.NetworkConnectionInterceptor
import com.example.tvnavigation.data.network.responses.AdvertisementsResponse
import com.example.tvnavigation.data.network.responses.LocationsResponse
import com.example.tvnavigation.data.network.responses.LoginResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface PlayerService {

   // Example - /api/location/kennwaithaka@gmail.com
   @GET("location/{email}")
   suspend fun getLocationsByEmail(
      @Path("email") email : String
   ): LocationsResponse

   @FormUrlEncoded
   @POST("location/login")
   suspend fun authenticateUser(
      @Field("id") id: String,
      @Field("password") password: String
   ): LoginResponse

   @GET("location/adverts")
   suspend fun fetchCurrentAdverts(

   ): AdvertisementsResponse

   companion object {

      /**
       * Dependency Injection - Pass in instance of interceptor which checks for internet connection
       * status and handles all exceptions
       *
       * @param networkConnectionInterceptor
       * @return LocationsService - Instance that calls api to get locations list
       */
      operator fun invoke(
            networkConnectionInterceptor: NetworkConnectionInterceptor
      ): PlayerService {
         val httpErrorInterceptor = HttpErrorInterceptor()
         val okHttpClient = OkHttpClient.Builder()
               .addInterceptor(networkConnectionInterceptor)
               .addInterceptor(httpErrorInterceptor)
               .build()

         return Retrofit.Builder()
               .client(okHttpClient)
               .baseUrl("https://youtise-location-dev.herokuapp.com/api/")
               .addConverterFactory(GsonConverterFactory.create())
               .build()
               .create(PlayerService::class.java)
      }
   }
}