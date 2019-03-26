package com.example.tvnavigation

import android.app.Application
import com.example.tvnavigation.data.db.YoutisePlayerDatabase
import com.example.tvnavigation.data.network.ConnectivityInterceptor
import com.example.tvnavigation.data.network.ConnectivityInterceptorImpl
import com.example.tvnavigation.data.network.services.LocationsService
import com.example.tvnavigation.data.repository.LocationsRepository
import com.example.tvnavigation.data.repository.LocationsRepositoryImpl
import com.example.tvnavigation.data.repository.datasources.LocationsDataSource
import com.example.tvnavigation.data.repository.datasources.LocationsDataSourceImpl
import com.example.tvnavigation.ui.viewmodels.LocationsVMFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class YoutisePlayerApplication: Application(), KodeinAware {
   override val kodein: Kodein = Kodein.lazy {
      import(androidXModule(this@YoutisePlayerApplication))

      /**
       * the instance fn provides the application context to all constructors
       */
      bind() from singleton { YoutisePlayerDatabase(instance()) }
      bind() from singleton { instance<YoutisePlayerDatabase>().locationDao() }
      bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }
      bind() from singleton { LocationsService(instance()) }
      bind<LocationsDataSource>() with singleton { LocationsDataSourceImpl(instance()) }
      bind<LocationsRepository>() with singleton { LocationsRepositoryImpl(instance(), instance()) }
      bind() from provider { LocationsVMFactory(instance()) }
   }

/*   override fun onCreate() {
      super.onCreate()


   }*/
}