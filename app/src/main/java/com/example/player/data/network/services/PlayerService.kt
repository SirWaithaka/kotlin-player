package com.example.player.data.network.services

import com.example.player.data.network.interceptors.AuthenticationInterceptor
import com.example.player.data.network.interceptors.ClientRequestInterceptor
import com.example.player.data.network.interceptors.HttpErrorInterceptor
import com.example.player.data.network.responses.AdvertisementsResponse
import okhttp3.OkHttpClient
import retrofit2.http.*

interface PlayerService {

   @GET("location/adverts")
   suspend fun fetchCurrentAdverts(): AdvertisementsResponse

   @FormUrlEncoded
   @POST("analytics/create-adlog")
   suspend fun onAdvertChange(
      @Field("adId") id: String,
      @Field("playTime") startTime: String,
      @Field("stopTime") endTime: String,
      @Field("duration") result: String
//      @Field("duration") duration: String
   )

   @FormUrlEncoded
   @POST("location/ad-changed")
   suspend fun invokePopCapture(
      @Field("advertId") id: String
   )

   companion object {

      /**
       * Dependency Injection - Pass in instance of interceptor which checks for internet connection
       * status and handles all exceptions
       *
       * @param networkConnectionInterceptor
       * @return LocationsService - Instance that calls api to get locations list
       */
      operator fun invoke(
            networkConnectionInterceptor: ClientRequestInterceptor,
            authenticationInterceptor: AuthenticationInterceptor
      ): PlayerService {
         val httpErrorInterceptor = HttpErrorInterceptor()
         val okHttpClient = OkHttpClient.Builder()
               .addInterceptor(networkConnectionInterceptor)
               .addInterceptor(authenticationInterceptor)
               .authenticator(authenticationInterceptor)
               .addInterceptor(httpErrorInterceptor)
               .build()

         val retrofit = RetrofitProviderFactory.instance()

         return retrofit.newBuilder()
            .client(okHttpClient)
            .build()
            .create(PlayerService::class.java)
      }
   }
}