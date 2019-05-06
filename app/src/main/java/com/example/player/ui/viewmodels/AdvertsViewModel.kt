package com.example.player.ui.viewmodels

import android.os.Environment
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.example.player.data.db.entities.Advert
import com.example.player.data.db.models.DeviceModel
import com.example.player.data.repository.AdvertsRepository
import com.example.player.internal.MINIMUM_STALE_THRESHOLD
import com.example.player.internal.SingleEvent
import com.example.player.internal.getLocalMediaPath
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

class AdvertsViewModel(
   private val advertsRepository: AdvertsRepository,
   private val deviceModel: DeviceModel
) : ScopedViewModel() {

   private val Tag = "AdvertsViewModel"
   private val zonedId = ZoneId.systemDefault()
   private val currentTime: ZonedDateTime
      get() = ZonedDateTime.ofInstant(Instant.now(), zonedId)
   private var advertsCount = 0
   private var playableAdverts = listOf<Advert>()
   private val mediaPath = Environment
      .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
      .toString()

   private val _downloadProgress = MutableLiveData<Int>()
   private val _hasDownloaded = MutableLiveData<SingleEvent<Boolean>>()
   private val _downloadedCount = MutableLiveData<Int>()

   private val toDownloadKey = "TO_DOWNLOAD"
   private val toDeleteKey = "TO_DELETE"

   private val retrievedAdverts: List<Advert>
      get() = runBlocking {
         withContext(super.coroutineContext) {
            return@withContext getAdverts()
         }
      }

   init {
      advertsRepository.setAdvertsFetchedListener(object : AdvertsRepository.AdvertsFetchedListener {
         override fun onAdvertsFetched(adverts: List<Advert>) {
            advertsCount = adverts.size

            val sortedAdvertHashMap = compareAdverts(adverts, retrievedAdverts)
            val toDownload: List<Advert>  = sortedAdvertHashMap[toDownloadKey]!!

            if (toDownload.isNotEmpty())
               downloadAdverts(toDownload)
             else
               _hasDownloaded.value = SingleEvent(true)
            setPlayableAdverts(adverts)

            deleteStaleAdverts(sortedAdvertHashMap[toDeleteKey]!!)
         }

         override fun onAdvertsPersisted(status: Boolean) = Unit
      })
   }

   /*
    * Compares list of adverts fetched from api with
    * list of adverts stored in the local database
    * check if we have new adverts incoming
    * check if we have old adverts in retrieved
    */
   private fun compareAdverts(
      fetchedAdverts: List<Advert>,
      retrievedAdverts: List<Advert>
   ): HashMap<String, List<Advert>> {
      val toDownload = fetchedAdverts.minus(retrievedAdverts)
      val toDelete = retrievedAdverts.minus(fetchedAdverts)

      return hashMapOf(
         Pair(toDeleteKey, toDelete),
         Pair(toDownloadKey, toDownload)
      )
   }

   private fun deleteStaleAdverts(staleAdverts: List<Advert>) {
      if (staleAdverts.isNotEmpty()) {
         val filePaths = mutableListOf<String>()
         for (staleAd in staleAdverts) {
            filePaths.add(staleAd.getLocalMediaPath())
         }
      }
   }

   private fun downloadAdverts(adverts: List<Advert>) {
      var count = 0
      var totalProgress = 0
      for (ad in adverts) {

         PRDownloader.download(ad.mediaURL, mediaPath, ad.fileName)
            .build()
            .setOnProgressListener { progress ->
               val progressPercent: Long = progress.currentBytes * 100 / progress.totalBytes

               if ((progressPercent % 20.toLong()) == 0.toLong()) {
                  totalProgress += 1
                  _downloadProgress.postValue(totalProgress)
               }
            }
            .start(object : OnDownloadListener {
               override fun onDownloadComplete() {

                  count += 1
                  _downloadedCount.postValue(count)
                  if (count == adverts.size) {
                     deviceModel.lastDownloadDate = currentTime
                     _hasDownloaded.postValue(SingleEvent(true))
                  }
               }

               override fun onError(error: Error?) {
//                  Log.d(TAG, "Error occured: ${error?.isConnectionError}")
               }
            })
      }
   }

   fun getPlayableAdverts(): List<Advert> {
      return playableAdverts
   }

   fun setPlayableAdverts(toPlay: List<Advert>) {
      playableAdverts = toPlay
   }

   fun getDownloadInfo(): MediatorLiveData<MergedData> {
      val downloadInfo = MediatorLiveData<MergedData>()
      downloadInfo.addSource(_downloadProgress) {
         downloadInfo.value = MergedData.CountInfo(it)
      }
      downloadInfo.addSource(_hasDownloaded) {
         downloadInfo.value = MergedData.HasDownloadedStatus(it.getContent())
      }
      downloadInfo.addSource(_downloadedCount) {
         downloadInfo.value = MergedData.DownloadedCount(it)
      }

      return downloadInfo
   }

   suspend fun getAdverts(): List<Advert> {
      return advertsRepository.retrieveAdverts()
   }

   suspend fun fetchAdverts() {
      advertsRepository.fetchAdverts()
   }

   fun isStale(): Boolean {
      val lastUpdatedTime = deviceModel.lastDownloadDate
      val surpassedStaleThreshold: Boolean
      surpassedStaleThreshold = if (lastUpdatedTime != null)
         (lastUpdatedTime.hour - currentTime.hour) > MINIMUM_STALE_THRESHOLD
      else true
      return surpassedStaleThreshold
   }

   sealed class MergedData {
      data class CountInfo(val count: Int) : MergedData()
      data class HasDownloadedStatus(val hasDownloaded: Boolean?) : MergedData()
      data class DownloadedCount(val count: Int) : MergedData()
   }
}