package com.example.player.data.network.apiservices

import com.example.player.data.network.interceptors.ClientRequestInterceptor
import com.example.player.data.network.interceptors.HttpErrorInterceptor
import com.example.player.data.network.responses.LoginResponse
import okhttp3.OkHttpClient
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthorizationApiService {

   @FormUrlEncoded
   @POST("location/login")
   suspend fun authenticateUser(
      @Field("id") id: String,
      @Field("serialNumber") serial: String,
      @Field("password") password: String
   ): LoginResponse

   companion object {

      /**
       * Dependency Injection - Pass in instance of interceptor which checks for internet connection
       * status and handles all exceptions
       *
       * @param networkConnectionInterceptor
       * @return AuthorizationApiService - Instance that calls api to get locations list
       */
      operator fun invoke(
         networkConnectionInterceptor: ClientRequestInterceptor
      ): AuthorizationApiService {
         val httpErrorInterceptor = HttpErrorInterceptor()
         val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(networkConnectionInterceptor)
            .addInterceptor(httpErrorInterceptor)
            .build()

         val retrofit = RetrofitProviderFactory.instance()

         return retrofit.newBuilder()
            .client(okHttpClient)
            .build()
            .create(AuthorizationApiService::class.java)
      }
   }

}