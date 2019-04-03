package com.example.tvnavigation.data.network.interceptors

import com.example.tvnavigation.data.db.LocationDao
import com.example.tvnavigation.data.db.entities.Device
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class AuthenticationInterceptor(
      private val locationDao: LocationDao
): Authenticator {

   private var authenticationToken = ""
   private val device: Device by lazy { locationDao.getDeviceInfo() }

   /**
    * TODO("Add functionality to refresh token")
    */
   override fun authenticate(route: Route?, response: Response): Request? {

      // check if response code is 401
      if (response.code() == 401) {
         authenticationToken = device.authToken
         val original =  response.request()
         val request: Request = original
            .newBuilder()
            .header("Authorization", "Bearer $authenticationToken")
            .build()
         return request
      }
      return null
   }

}