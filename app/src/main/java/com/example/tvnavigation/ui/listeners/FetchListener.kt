//package com.example.tvnavigation.ui.listeners
//
//import android.util.Log
//import com.tonyodev.fetch2.Download
//import com.tonyodev.fetch2.Error
//import com.tonyodev.fetch2.FetchListener
//import com.tonyodev.fetch2core.DownloadBlock
//
//abstract class FetchListener: FetchListener {
//
//   override fun onAdded(download: Download) {
//
//   }
//
//   abstract override fun onStarted(download: Download, downloadBlocks: List<DownloadBlock>, totalBlocks: Int)
//
//   abstract override fun onProgress(download: Download, etaInMilliSeconds: Long, downloadedBytesPerSecond: Long)
//
//   abstract override fun onCompleted(download: Download)
//
//   abstract override fun onError(download: Download, error: Error, throwable: Throwable?)
//
//   override fun onCancelled(download: Download) {
//
//   }
//
//   override fun onDeleted(download: Download) {
//
//   }
//
//   override fun onDownloadBlockUpdated(download: Download, downloadBlock: DownloadBlock, totalBlocks: Int) {
//
//   }
//
//   override fun onPaused(download: Download) {
//
//   }
//
//   override fun onQueued(download: Download, waitingOnNetwork: Boolean) {
//      Log.d("DownloadFragment", "Download queued")
//   }
//
//   override fun onRemoved(download: Download) {
//
//   }
//
//   override fun onResumed(download: Download) {
//
//   }
//
//   override fun onWaitingNetwork(download: Download) {
//   }
//}