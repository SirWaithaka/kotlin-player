package com.example.player.data.network.apiservices

import com.example.player.data.network.interceptors.AuthenticationInterceptor
import com.example.player.data.network.interceptors.ClientRequestInterceptor
import com.example.player.data.network.interceptors.HttpErrorInterceptor
import com.example.player.data.network.responses.AdvertisementsResponse
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface PlayerApiService {

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

   @Multipart
   @POST("analytics/record-ad-analytic")
   suspend fun uploadFile(
      @Part("adId") advertId: RequestBody,
      @Part file: MultipartBody.Part
   ): ResponseBody



   companion object {

      /**
       * Dependency Injection - Pass in instance of interceptor which checks for internet connection
       * status and handles all exceptions
       *
       * @param networkConnectionInterceptor
       * @return LocationsApiService - Instance that calls api to get locations list
       */
      operator fun invoke(
            networkConnectionInterceptor: ClientRequestInterceptor,
            authenticationInterceptor: AuthenticationInterceptor
      ): PlayerApiService {
         val httpErrorInterceptor = HttpErrorInterceptor()
         val okHttpClient = OkHttpClient.Builder()
               .readTimeout(180, TimeUnit.SECONDS)
               .writeTimeout(180, TimeUnit.SECONDS)
               .addInterceptor(networkConnectionInterceptor)
               .addInterceptor(authenticationInterceptor)
               .authenticator(authenticationInterceptor)
               .addInterceptor(httpErrorInterceptor)
               .build()

         val retrofit = RetrofitProviderFactory.instance()

         return retrofit.newBuilder()
            .client(okHttpClient)
            .build()
            .create(PlayerApiService::class.java)
      }
   }
}