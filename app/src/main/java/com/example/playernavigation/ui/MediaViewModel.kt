package com.example.playernavigation.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playernavigation.models.MediaType

class MediaViewModel : ViewModel() {
   val media = MutableLiveData<MediaType>()
   private var counter = 1
   private var item: Int = 0
   private val myArray = ArrayList<Int>(15)
   fun initArray() {
      for (i in 1..15) {
         myArray.add(i)
      }
      
      item = myArray.get(0)
   }

   fun changeAd(index: Int) {

   }

}