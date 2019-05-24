package com.example.player.ui.viewmodels

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.example.player.data.db.entities.Advert
import com.example.player.data.db.models.DeviceModel
import com.example.player.data.repository.AdvertsRepository
import com.example.player.internal.*
import kotlinx.coroutines.*
import java.io.File

class AdvertsViewModel(
   private val advertsRepository: AdvertsRepository,
   private val deviceModel: DeviceModel
) : ScopedViewModel() {

//   private val Tag = "AdvertsViewModel"

   private val _downloadProgress = MutableLiveData<Int>()
   private val _hasDownloaded = MutableLiveData<SingleEvent<Boolean>>()
   private val _downloadedCount = MutableLiveData<Int>()

   private val retrievedAdverts: List<Advert>
      get() = runBlocking {
         withContext(super.coroutineContext) {
            return@withContext advertsRepository.retrieveAdverts()
         }
      }

   init {
      advertsRepository.setAdvertsFetchedListener(AdvertsFetchedListener())
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
   ): Pair<List<Advert>, List<Advert>> {
      val toDownload = fetchedAdverts.minus(retrievedAdverts)
      val toDelete = retrievedAdverts.minus(fetchedAdverts)

      return toDownload to toDelete
   }

   private fun deleteStaleAdverts(staleAdverts: List<Advert>) {
      if (staleAdverts.isNotEmpty()) {
         for (staleAd in staleAdverts) {
            File(staleAd.getLocalMediaPath()).delete()
         }
      }
   }

   private fun downloadAdverts(adverts: List<Advert>) {
      var count = 0
      var totalProgress = 0
      for (ad in adverts) {

         PRDownloader.download(ad.mediaURL, DOWNLOADS_DIR, ad.fileName)
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
                     deviceModel.lastDownloadDate = CURRENT_TIME
                     _hasDownloaded.postValue(SingleEvent(true))
                  }
               }

               override fun onError(error: Error?) = Unit
            })
      }
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

   suspend fun getAdvertsState() {

//      if (isStale()) return AdvertsState.StaleStatus(true)

      val mediaDirectory = File(DOWNLOADS_DIR)
      val mediaFileNames = mutableListOf<String>().also {
         for (file in mediaDirectory.listFiles()) {
            it.add(file.name)
         }
      }

      advertsRepository.retrieveAdverts().also { retrieved ->
         val keys = mutableListOf<String>()
         for (ad in retrieved) {
            keys.add(ad.fileName)
         }

         keys.minus(mediaFileNames).also { diff ->
            for (ad in diff) {
//               Log.d(Tag, ad)
            }
         }
      }
   }

   suspend fun fetchAdverts() {
      advertsRepository.fetchAdverts()
   }

   fun isStale(): Boolean {
      val lastUpdatedTime = deviceModel.lastDownloadDate ?: return true
      if (lastUpdatedTime.toEpochSecond() < START_OF_DAY.toEpochSecond()) return true
      if (retrievedAdverts.isEmpty()) return true
      return (lastUpdatedTime.hour - CURRENT_TIME.hour) > MINIMUM_STALE_THRESHOLD
   }

   private inner class AdvertsFetchedListener : AdvertsRepository.AdvertsFetchedListener {
      override fun onAdvertsFetched(adverts: List<Advert>) {

         val (incomingAdverts,toDelete) = compareAdverts(adverts, retrievedAdverts)

         if (incomingAdverts.isNotEmpty())
            downloadAdverts(incomingAdverts)
         else
            _hasDownloaded.value = SingleEvent(true)

         deleteStaleAdverts(toDelete)
      }

      override fun onAdvertsPersisted(status: Boolean) = Unit
   }

   sealed class MergedData {
      data class CountInfo(val count: Int) : MergedData()
      data class HasDownloadedStatus(val hasDownloaded: Boolean?) : MergedData()
      data class DownloadedCount(val count: Int) : MergedData()
   }
}