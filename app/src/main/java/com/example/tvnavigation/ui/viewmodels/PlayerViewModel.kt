package com.example.tvnavigation.ui.viewmodels

import android.os.Environment
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.tvnavigation.data.db.entities.Advert
import com.example.tvnavigation.data.network.AdvertLog
import com.example.tvnavigation.data.repository.AdvertsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import java.util.*
import kotlin.coroutines.CoroutineContext

class PlayerViewModel(
      private val advertsRepository: AdvertsRepository
): ViewModel(), CoroutineScope {

   private var job = Job()
   private var mediaToPlayPath = ""
   private var mediaStack: Stack<Advert>? = null
   private lateinit var startTime: ZonedDateTime
   private lateinit var stopTime: ZonedDateTime
   private lateinit var mediaToPlay: Advert

   private val mediaPath = Environment
      .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
      .toString()

   override val coroutineContext: CoroutineContext
      get() = job + Dispatchers.IO

   fun getMediaToPlayPath(): String {
      return mediaToPlayPath
   }

   fun setMediaToPlay(media: Advert) {
      mediaToPlay = media
      val fileName = media.mediaKey.split("/")[1]
      mediaToPlayPath = "$mediaPath/$fileName"
   }

   fun mediaAboutToPlayEvent() {
      launch {
         advertsRepository.invokePopCapture(mediaToPlay.id)
      }
   }

   fun mediaHasPlayedEvent() {
      launch {
         val log = AdvertLog(
            start = Date.from(startTime.toInstant()).toString(),
            end = Date.from(stopTime.toInstant()).toString(),
            result = "SUCCEED",
            id = mediaToPlay.id
         )
         Log.d("PlayerViewModel", mediaToPlay.adName)
         advertsRepository.postAdvertLog(log)
      }
   }

   fun setStartTime(now: ZonedDateTime) {
      startTime = now
   }

   fun setStopTime(now: ZonedDateTime) {
      stopTime = now
   }

   fun initStack(mediaList: List<Advert>) {
      if (mediaStack == null || isMediaStackEmpty())
         mediaStack = Stack(mediaList)
   }

   fun getMediaToPlay(): Advert {
      return mediaStack!!.pop()
   }

   fun isMediaStackEmpty(): Boolean {
      return mediaStack!!.isEmpty()
   }

   private inner class Stack<A>(list: List<A>) {

      private val elements: MutableList<A> = list.toMutableList()

      init {
         if (list.isEmpty()) {
            // do something when list is empty
         }
      }

      fun isEmpty() = elements.isEmpty()

      fun size() = elements.size

      fun pop(): A {
         val item = elements.last()
         if (!isEmpty()) {
            elements.removeAt(elements.size -1)
         }
         return item
      }

      fun peek(): Any? = elements.lastOrNull()

      override fun toString(): String = elements.toString()
   }

}