package com.youtise.player.data.network.interceptors

import android.content.Context
import android.net.ConnectivityManager
import com.youtise.player.internal.NoConnectivityException
import okhttp3.Interceptor
import okhttp3.Response


/**
 * Implementation that checks for internet connectivity status
 * from the device through the application context.
 *
 * @param context Type Context
 */
class NetworkConnectionInterceptor(context: Context) : ClientRequestInterceptor {

   private val appContext = context.applicationContext

   /**
    * @throws NoConnectivityException - IOException Type
    * @return Response object
    */
   override fun intercept(chain: Interceptor.Chain): Response {
      if (!isOnline())
         throw NoConnectivityException()

      return chain.proceed(chain.request())

   }

   /**
    * @return Boolean - If the device is online or not
    */
   private fun isOnline(): Boolean {
      val connectivityManager = appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
      val networkInfo = connectivityManager.activeNetworkInfo

      return networkInfo != null && networkInfo.isConnected
   }
}