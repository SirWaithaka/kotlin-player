package com.example.tvnavigation.data.network.interceptors

import android.util.Log
import com.example.tvnavigation.internal.ClientErrorException
import com.example.tvnavigation.internal.ServerErrorException
import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.Response

class HttpErrorInterceptor: ServerResponseInterceptor {

   private val gson: Gson = Gson()
   // class that handles passing json response to Object
   inner class ErrorResponse(
      val message: String,
      val error: String
   )

   override fun intercept(chain: Interceptor.Chain): Response {

      val response = chain.proceed(chain.request())
      if (response.isSuccessful)
         return response

      val jsonErrorResponse = response.body()!!.string()
      val errorResponse = gson.fromJson(jsonErrorResponse, ErrorResponse::class.java)
      when (response.code()) {
         // Handle Unauthorized
         401 -> throw ClientErrorException(errorResponse.message)
         // Handle Forbidden
         403 -> throw ClientErrorException(errorResponse.message)
         // Handle Not Found
         404 -> throw ClientErrorException(errorResponse.message)
         // Handle Method Not Allowed
         405 -> throw ClientErrorException(errorResponse.message)
         // Handle Internal Server Error
         500 -> throw ServerErrorException(errorResponse.message)
         // Catch unhandled error
         else -> throw ServerErrorException(errorResponse.message)
      }
   }
}