package com.example.tvnavigation.ui.viewmodels

import android.os.Environment
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.example.tvnavigation.data.db.entities.Advert
import com.example.tvnavigation.data.repository.AdvertsRepository
import com.example.tvnavigation.data.repository.DeviceRepository
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

class AdvertsViewModel(
   private val advertsRepository: AdvertsRepository,
   private val deviceRepository: DeviceRepository
) : ViewModel() {

//   private val TAG = "AdvertsViewModel"
   private val minimumStaleThreshold = 23
   private val zonedId = ZoneId.systemDefault()
   //   private val startOfDay = ZonedDateTime.ofInstant(Instant.now(), zonedId).toLocalDate().atStartOfDay(zonedId)
   private val currentTime: ZonedDateTime
      get() = ZonedDateTime.ofInstant(Instant.now(), zonedId)
   private var advertsCount = 0
   private var retrievedAdverts = listOf<Advert>()
   private var playableAdverts = listOf<Advert>()
   private val mediaPath = Environment
      .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
      .toString()

   private val _downloadProgress = MutableLiveData<Int>()
   private val _hasDownloaded = MutableLiveData<Boolean>()
   private val _downloadedCount = MutableLiveData<Int>()

   init {
      advertsRepository.setAdvertsFetchedListener(object : AdvertsRepository.AdvertsFetchedListener {
         override fun onAdvertsFetched(adverts: List<Advert>) {
            advertsCount = adverts.size
            compareAdverts(adverts, retrievedAdverts)
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
   private fun compareAdverts(fetchedAdverts: List<Advert>, retrievedAdverts: List<Advert>) {
      val toDownload = fetchedAdverts.minus(retrievedAdverts)
      val toDelete = retrievedAdverts.minus(fetchedAdverts)
      if (toDownload.isNotEmpty()) {
         downloadAdverts(toDownload)
         setPlayableAdverts(fetchedAdverts)
      } else {
         setPlayableAdverts(fetchedAdverts)
         _hasDownloaded.value = true
      }

      if (toDelete.isNotEmpty())
         deleteStaleAdverts(toDelete)
   }

   private fun deleteStaleAdverts(staleAdverts: List<Advert>) {

   }

   private fun downloadAdverts(adverts: List<Advert>) {
      var count = 0
      var totalProgress = 0
      for (ad in adverts) {
         val fileName = ad.mediaKey.split("/")[1]

         PRDownloader.download(ad.mediaURL, mediaPath, fileName)
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
                     deviceRepository.setLastUpdated(currentTime)
                     _hasDownloaded.postValue(true)
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
         downloadInfo.value = MergedData.DownloadedInfo(it)
      }
      downloadInfo.addSource(_downloadedCount) {
         downloadInfo.value = MergedData.DownloadedCount(it)
      }

      return downloadInfo
   }

   suspend fun getAdverts(): List<Advert> {
      retrievedAdverts = advertsRepository.retrieveAdverts()
      return retrievedAdverts
   }

   suspend fun fetchAdverts() {
      advertsRepository.fetchAdverts()
   }

   suspend fun isStale(): Boolean {
      val lastUpdatedTime = deviceRepository.getDeviceInfo().lastUpdated
      var surpassedStaleThreshold = true
      if (lastUpdatedTime != null)
         surpassedStaleThreshold = (lastUpdatedTime.hour - currentTime.hour) > minimumStaleThreshold
//      return (currentTime.hour - startOfDay.hour) >  || lastUpdatedTime == null
      return surpassedStaleThreshold || lastUpdatedTime == null
   }

   sealed class MergedData {
      data class CountInfo(val count: Int) : MergedData()
      data class DownloadedInfo(val hasDownloaded: Boolean) : MergedData()
      data class DownloadedCount(val count: Int) : MergedData()
   }
}