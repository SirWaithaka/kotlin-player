package com.example.player.ui.viewmodels

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.example.player.data.db.entities.Advert
import com.example.player.data.db.models.MediaModel
import com.example.player.data.network.AdvertLog
import com.example.player.data.repository.AdvertsRepository
import com.example.player.internal.getLocalMediaPath
import com.example.player.services.ImageCaptureService
import kotlinx.coroutines.*
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
   var mediaPlaylist: List<MediaModel>? = null


   override val coroutineContext: CoroutineContext
      get() = job + Dispatchers.IO

   private fun buildMediaPlaylist(adverts: List<Advert>): List<MediaModel> {
      val mediaList = mutableListOf<MediaModel>()
      for (ad in adverts) {
         mediaList.add(
            MediaModel(
               id = ad.id,
               name = ad.adName,
               type = ad.mediaType,
               timesOfDay = ad.timeOfDay,
               localMediaPath = ad.getLocalMediaPath()
            )
         )
      }
      return mediaList
   }

   fun getPlayableAdverts() : List<MediaModel> = runBlocking {
      buildMediaPlaylist(advertsRepository.retrieveAdverts())
   }

   fun getMediaToPlayPath(): String {
      return mediaToPlay.localMediaPath
   }

   fun setMediaToPlay(media: MediaModel) {
      mediaToPlay = media
   }

   fun mediaAboutToPlayEvent(context: Context) {
      launch {
         advertsRepository.invokePopCapture(mediaToPlay.id)
         startService(context, mediaToPlay.id)
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

   /*
    *
    * TODO("Fix the bug with looping indefinitely")
    * This function loops indefinitely when the return condition always returns false
    * - Case when there is not media to play at current time
    * Implement a Queue or better data structure to hold the media playlist.
    */
   tailrec fun getMediaToPlay(): MediaModel {

      if (mediaStack == null || isMediaStackEmpty())
         initStack()

      val media = mediaStack!!.pop()
      return when (media.isPlayable) {
         true -> media
         else -> getMediaToPlay()
      }
   }

   private fun initStack() {
      mediaStack = Stack(mediaPlaylist!!)
   }

   private fun isMediaStackEmpty(): Boolean {
      return mediaStack!!.isEmpty()
   }

   private fun startService(context: Context, id: String) {

      Intent()
      val serviceIntent = Intent(context, ImageCaptureService::class.java)
      serviceIntent.putExtra("inputExtra", id)

      ContextCompat.startForegroundService(context, serviceIntent)
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