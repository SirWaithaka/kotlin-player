package com.example.tvnavigation.data.network.interceptors

import com.example.tvnavigation.data.db.DeviceDao
import com.example.tvnavigation.data.db.entities.Device
import com.example.tvnavigation.data.network.responses.LoginResponse
import kotlinx.coroutines.*
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


class AuthenticationInterceptor(
      private val deviceDao: DeviceDao
): Authenticator {

   private var baseUrl = "https://youtise-location-dev.herokuapp.com/api/"
   private var requestCount = 0
   private var authenticationToken = ""
   private val device: Device by lazy { deviceDao.getDeviceInfo() }

   /**
    * This function just intercepts request that are unauthorized
    * It retrieves the authtoken from the db and adds it to the header
    *
    * TODO("Add functionality to refresh token")
    */
   override fun authenticate(route: Route?, response: Response): Request? {
      // check if response code is 401
      if (response.code() == 401) {
         if (requestCount == 6) {
            runBlocking {
               authenticationToken = requestToken(device.locationId)
            }
            persistToken(authenticationToken)
         }
         requestCount += 1
         authenticationToken = device.authToken
         val original =  response.request()
         return original
            .newBuilder()
            .header("Authorization", "Bearer $authenticationToken")
            .build()
      }
      return null
   }

   private fun persistToken(token: String) {
      GlobalScope.launch(Dispatchers.IO) {
         device.authToken = token
         deviceDao.upsertDeviceInfo(device)
      }
   }

   private suspend fun requestToken(id: String, otpCode: Int =1234): String {
      val service: AuthorizationService = RetrofitAuthenticator.getInstance(baseUrl)
      val response = service.authenticateUserAsync(id, otpCode.toString())
      return response.token
   }

   object RetrofitAuthenticator {

      private var service: AuthorizationService? = null

      fun getInstance(baseUrl: String): AuthorizationService {
         if (service == null) {
            service = Retrofit.Builder()
               .baseUrl(baseUrl)
               .addConverterFactory(GsonConverterFactory.create())
               .build()
               .create(AuthorizationService::class.java)
         }
         return service!!
      }
   }

   interface AuthorizationService {
      @FormUrlEncoded
      @POST("location/login")
      suspend fun authenticateUserAsync(
         @Field("id") id: String,
         @Field("password") password: String
      ): LoginResponse
   }

}