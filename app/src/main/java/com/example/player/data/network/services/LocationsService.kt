package com.example.player.data.network.services

import com.example.player.data.network.interceptors.ClientRequestInterceptor
import com.example.player.data.network.interceptors.HttpErrorInterceptor
import com.example.player.data.network.responses.LocationsResponse
import okhttp3.OkHttpClient
import retrofit2.http.*

interface LocationsService {

   // Example - /api/location/kennwaithaka@gmail.com
   @GET("location/{email}")
   suspend fun getLocationsByEmail(
      @Path("email") email: String
   ): LocationsResponse

   companion object {

      /**
       * Dependency Injection - Pass in instance of interceptor which checks for internet connection
       * status and handles all exceptions
       *
       * @param networkConnectionInterceptor
       * @return LocationsService - Instance that calls api to get locations list
       */
      operator fun invoke(
         networkConnectionInterceptor: ClientRequestInterceptor
      ) : LocationsService {
         val httpErrorInterceptor = HttpErrorInterceptor()
         val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(networkConnectionInterceptor)
            .addInterceptor(httpErrorInterceptor)
            .build()

         val retrofit = RetrofitProviderFactory.instance()

         return retrofit.newBuilder()
            .client(okHttpClient)
            .build()
            .create(LocationsService::class.java)
      }
   }
}
