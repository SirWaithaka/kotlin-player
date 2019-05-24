package com.example.player.data.network.interceptors

import com.example.player.data.db.entities.Device
import com.example.player.data.db.models.DeviceModel
import com.example.player.data.network.apiservices.AuthorizationApiService
import kotlinx.coroutines.*
import okhttp3.*


class AuthenticationInterceptor(
      private val deviceModel: DeviceModel,
      private val authorizationApiService: AuthorizationApiService
): Authenticator, Interceptor {

   private var requestCount = 0
   private var authenticationToken = ""
   private val device: Device by lazy {
      runBlocking { deviceModel.retrieve() }
   }

   override fun intercept(chain: Interceptor.Chain): Response {
      val original = chain.request()
      original.header("Authorization") ?: return chain.proceed(original
         .newBuilder()
         .header("Authorization", "Bearer ${device.authToken}")
         .build()
      )
      return chain.proceed(original)
   }

   /**
    * This function just intercepts responses from the server and
    * checks the requests that are unauthorized
    * It retrieves the authtoken from the db and adds it to the header
    */
   override fun authenticate(route: Route?, response: Response): Request? {
      // check if response code is 401
      if (response.code() == 401) {
         if (requestCount == 6) {
            runBlocking {
               authenticationToken = requestToken(device.locationId, device.serialNumber)
            }
            persistToken(authenticationToken)
            requestCount = 0
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

   /*
    * Launches a coroutine that calls the Device Dao and updates
    * the record with the new token
    */
   private fun persistToken(token: String) {
         deviceModel.authToken = token
   }

   /*
    * Mundane implementation of refresh token functionality
    * Calls the authorizationApiService </api/login> gives it the required params
    * And gets a token from the endpoint
    */
   private suspend fun requestToken(id: String, serial: String, otpCode: Int = 1234): String {
      val response = authorizationApiService.authenticateUser(id, serial, otpCode.toString())
      return response.token
   }
}