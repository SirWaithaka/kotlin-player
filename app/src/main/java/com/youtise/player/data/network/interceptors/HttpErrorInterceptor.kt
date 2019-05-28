package com.youtise.player.data.network.interceptors

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.youtise.player.internal.ClientErrorException
import com.youtise.player.internal.ServerErrorException
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.Response

class HttpErrorInterceptor: ServerResponseInterceptor {

//   private val Tag = "HttpErrorInterceptor"
   private val gsonBuilder = GsonBuilder()
   private val gson: Gson

   init {
      gsonBuilder.setExclusionStrategies(ExcludeErrorField)
      gson = gsonBuilder.create()
   }

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
      val errorResponse by lazy { gson.fromJson(jsonErrorResponse, ErrorResponse::class.java) }

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
         // Handle Server Unavailable
         503 -> throw ServerErrorException("Server Unavailable")
         // Catch unhandled Exception
         else -> throw ServerErrorException("Unknown Error!")
      }
   }


   /*
    * Tell GSON to exclude parsing the error field for now
    */
   object ExcludeErrorField : ExclusionStrategy {
      override fun shouldSkipField(f: FieldAttributes?): Boolean {
         if ("error" == f?.name) {
            return true
         }
         return false
      }

      override fun shouldSkipClass(clazz: Class<*>?): Boolean {
         return false
      }
   }
}