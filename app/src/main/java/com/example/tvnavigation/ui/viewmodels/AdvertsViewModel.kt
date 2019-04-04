package com.example.tvnavigation.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tvnavigation.data.db.entities.Advert
import com.example.tvnavigation.data.repository.AdvertsRepository

class AdvertsViewModel(
   private val advertsRepository: AdvertsRepository
): ViewModel() {

   private val TAG = "AdvertsViewModel"
   private val mHasDownloadedAdverts = MutableLiveData<Boolean>()
   val hasDownloadedAdverts: LiveData<Boolean>
         get() = mHasDownloadedAdverts

   init {
      advertsRepository.setAdvertsFetchedListener(object: AdvertsRepository.AdvertsFetchedListener {
         override fun onAdvertsFetched(adverts: List<Advert>) {
            Log.d(TAG, "Downloaded list: $adverts")
         }

         override fun onAdvertsPersisted(status: Boolean) {

         }
      })
   }

   suspend fun getAdverts(): List<Advert> {
      return advertsRepository.retrieveAdverts()
   }

   suspend fun fetchAdverts() {
      advertsRepository.fetchAdverts()
   }

}