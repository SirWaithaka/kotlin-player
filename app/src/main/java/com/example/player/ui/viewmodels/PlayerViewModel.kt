package com.example.player.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.player.data.db.models.MediaModel
import com.example.player.data.network.AdvertLog
import com.example.player.data.repository.AdvertsRepository
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
   private var mediaStack: Stack<MediaModel>? = null
   private lateinit var startTime: ZonedDateTime
   private lateinit var stopTime: ZonedDateTime
   private lateinit var mediaToPlay: MediaModel


   override val coroutineContext: CoroutineContext
      get() = job + Dispatchers.IO

   fun getMediaToPlayPath(): String {
      return mediaToPlay.localMediaPath
   }

   fun setMediaToPlay(media: MediaModel) {
      mediaToPlay = media
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
         advertsRepository.postAdvertLog(log)
      }
   }

   fun setStartTime(now: ZonedDateTime) {
      startTime = now
   }

   fun setStopTime(now: ZonedDateTime) {
      stopTime = now
   }

   fun initStack(mediaList: List<MediaModel>) {
      if (mediaStack == null || isMediaStackEmpty())
         mediaStack = Stack(mediaList)
   }

   fun getMediaToPlay(): MediaModel {
      val media = mediaStack!!.pop()
      return when (media.isPlayable) {
         true -> media
         else -> getMediaToPlay()
      }
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

      fun peek(): A? = elements.lastOrNull()

      override fun toString(): String = elements.toString()
   }

}