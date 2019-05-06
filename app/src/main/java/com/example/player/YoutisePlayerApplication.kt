package com.example.player

import android.app.Application
import com.example.player.data.db.YoutisePlayerDatabase
import com.example.player.data.network.ErrorsHandler
import com.example.player.data.network.interceptors.*
import com.example.player.data.network.services.PlayerService
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
import com.example.player.data.network.services.AuthorizationService
import com.example.player.data.network.services.LocationsService

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
      bind() from singleton { LocationsService(instance()) }
      bind() from singleton { AuthorizationService(instance()) }
      bind() from singleton { PlayerService(instance(), instance()) }
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
   }
}