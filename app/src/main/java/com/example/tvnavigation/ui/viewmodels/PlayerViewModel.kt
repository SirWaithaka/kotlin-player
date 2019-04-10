package com.example.tvnavigation.ui.viewmodels

import android.os.Environment
import androidx.lifecycle.ViewModel
import com.example.tvnavigation.data.db.entities.Advert

class PlayerViewModel: ViewModel() {

   private var mediaToPlay = ""
   private var indexOfMediaToPlay = 0

   private val mediaPath = Environment
      .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
      .toString()

   fun getMediaToPlay(): String {
      return mediaToPlay
   }

   fun setMediaToPlay(media: Advert) {
      val fileName = media.mediaKey.split("/")[1]
      mediaToPlay = "$mediaPath/$fileName"
   }

   fun getIndexOfMediaToPlay(): Int {
      return indexOfMediaToPlay
   }

   fun setIndexOfMediaToPlay(index: Int) {
      indexOfMediaToPlay = index
   }
}