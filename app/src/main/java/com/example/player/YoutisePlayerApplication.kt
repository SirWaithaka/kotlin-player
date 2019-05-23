package com.example.player

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.example.player.data.db.YoutisePlayerDatabase
import com.example.player.data.network.ErrorsHandler
import com.example.player.data.network.interceptors.*
import com.example.player.data.network.apiservices.PlayerApiService
import com.example.player.data.repository.*
import com.example.player.data.repository.datasources.AdvertsNetworkDataSource
import com.example.player.data.repository.datasources.AdvertsNetworkDataSourceImpl
import com.example.player.data.repository.datasources.LocationsDataSource
import com.example.player.data.repository.datasources.LocationsDataSourceImpl
import com.example.player.ui.viewmodels.ViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import com.downloader.PRDownloader
import com.downloader.PRDownloaderConfig
import com.example.player.data.db.models.DeviceModel
import com.example.player.data.network.apiservices.AuthorizationApiService
import com.example.player.data.network.apiservices.LocationsApiService
import com.example.player.internal.CHANNEL_ID
import com.example.player.internal.IMAGE_CAPTURE_SERVICE_NAME
import com.jakewharton.threetenabp.AndroidThreeTen

class YoutisePlayerApplication: Application(), KodeinAware {
   override val kodein: Kodein = Kodein.lazy {
      import(androidXModule(this@YoutisePlayerApplication))

      /**
       * the instance fn provides the application context to all constructors
       * Defines the singletons accessible throughout the application context
       */
      // create instance of database and pass application context
      bind() from singleton { YoutisePlayerDatabase(instance()) }
      bind() from singleton { instance<YoutisePlayerDatabase>().locationDao() }
      bind() from singleton { instance<YoutisePlayerDatabase>().advertDao() }
      bind() from singleton { instance<YoutisePlayerDatabase>().deviceDao() }

      bind() from singleton { DeviceModel(instance()) }

      bind<ClientRequestInterceptor>() with singleton {
         // create instance and pass application context
         NetworkConnectionInterceptor(instance())
      }
      bind() from singleton { AuthenticationInterceptor(instance(), instance()) }
      bind<ServerResponseInterceptor>() with singleton {
         HttpErrorInterceptor()
      }
      bind() from singleton { LocationsApiService(instance()) }
      bind() from singleton { AuthorizationApiService(instance()) }
      bind() from singleton { PlayerApiService(instance(), instance()) }
      bind<LocationsDataSource>() with singleton { LocationsDataSourceImpl(instance(), instance()) }
      bind<AdvertsNetworkDataSource>() with singleton { AdvertsNetworkDataSourceImpl(instance()) }
      bind<LocationsRepository>() with singleton { LocationsRepositoryImpl(instance(), instance()) }
      bind<AdvertsRepository>() with singleton { AdvertsRepositoryImpl(instance(), instance())}
      bind<DeviceRepository>() with singleton { DeviceRepositoryImpl(instance(), instance()) }
      bind() from singleton { ErrorsHandler(instance(), instance()) }
      bind() from provider { ViewModelFactory(instance(), instance(), instance(), instance(), instance()) }
   }

   override fun onCreate() {
      super.onCreate()

      val config = PRDownloaderConfig.newBuilder()
         .setDatabaseEnabled(true)
         .build()
      PRDownloader.initialize(this, config)

      createNotificationChannel()

      // initialize Threeten Module
      AndroidThreeTen.init(this)
   }

   private fun createNotificationChannel() {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
         val serviceChannel = NotificationChannel(
            CHANNEL_ID,
            IMAGE_CAPTURE_SERVICE_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
         )

         val notifManager = getSystemService(NotificationManager::class.java)
         notifManager.createNotificationChannel(serviceChannel)
      }
   }
}