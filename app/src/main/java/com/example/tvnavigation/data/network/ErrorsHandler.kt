package com.example.tvnavigation.data.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tvnavigation.data.repository.datasources.AdvertsNetworkDataSource
import com.example.tvnavigation.data.repository.datasources.LocationsDataSource

class ErrorsHandler(
      locationsDataSource: LocationsDataSource,
      advertsNetworkDataSource: AdvertsNetworkDataSource
) {

   private var mHttpErrorResponse = MutableLiveData<String>()
   private val httpErrorResponse: LiveData<String>
      get() = mHttpErrorResponse

   init {
      locationsDataSource.httpErrorResponse.observeForever {
         mHttpErrorResponse.value = it
      }
      advertsNetworkDataSource.httpErrorResponse.observeForever {
         mHttpErrorResponse.value = it
      }
   }

   fun getHttpErrorResponses(): LiveData<String> {
      return this.httpErrorResponse
   }

}