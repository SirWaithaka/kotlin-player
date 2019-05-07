package com.example.player.data.network.apiservices

import com.example.player.data.network.interceptors.ClientRequestInterceptor
import com.example.player.data.network.interceptors.HttpErrorInterceptor
import com.example.player.data.network.responses.LocationsResponse
import okhttp3.OkHttpClient
import retrofit2.http.*

interface LocationsApiService {

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
       * @return LocationsApiService - Instance that calls api to get locations list
       */
      operator fun invoke(
         networkConnectionInterceptor: ClientRequestInterceptor
      ) : LocationsApiService {
         val httpErrorInterceptor = HttpErrorInterceptor()
         val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(networkConnectionInterceptor)
            .addInterceptor(httpErrorInterceptor)
            .build()

         val retrofit = RetrofitProviderFactory.instance()

         return retrofit.newBuilder()
            .client(okHttpClient)
            .build()
            .create(LocationsApiService::class.java)
      }
   }
}
