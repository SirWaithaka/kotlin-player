package com.example.tvnavigation.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tvnavigation.data.repository.AdvertsRepository

class AdvertsViewModel(
   advertsRepository: AdvertsRepository
): ViewModel() {

   private val mHasDownloadedAdverts = MutableLiveData<Boolean>()
   val hasDownloadedAdverts: LiveData<Boolean>
         get() = mHasDownloadedAdverts



}