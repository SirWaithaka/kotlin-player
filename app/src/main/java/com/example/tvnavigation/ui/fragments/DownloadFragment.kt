package com.example.tvnavigation.ui.fragments

import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.downloader.*
import com.example.tvnavigation.R
import com.example.tvnavigation.ui.base.ScopedFragment
import com.example.tvnavigation.ui.viewmodels.AdvertsViewModel
import com.example.tvnavigation.ui.viewmodels.ViewModelFactory
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import java.time.ZonedDateTime

class DownloadFragment: ScopedFragment(), KodeinAware {

   private val TAG = "DownloadFragment"
   override val kodein: Kodein by kodein()
   private val viewModelFactory: ViewModelFactory by instance()
   private lateinit var viewModel: AdvertsViewModel
   private lateinit var textView: TextView
   private lateinit var progressBar: ProgressBar
   private var advertsCount = 0
   private var isSetprogressBarSize = false

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      Log.d(TAG, ZonedDateTime.now().toLocalDateTime().minusHours(23).toString())
      Log.d(TAG, ZonedDateTime.now().toLocalDateTime().toString())
      Log.d(TAG, ZonedDateTime.now().toLocalDate().atStartOfDay().toString())
   }

   override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
      return inflater.inflate(R.layout.fragment_download, container, false)
   }

   override fun onActivityCreated(savedInstanceState: Bundle?) {
      super.onActivityCreated(savedInstanceState)

      viewModel = ViewModelProviders.of(activity!!, viewModelFactory).get(AdvertsViewModel::class.java)
      textView = view!!.findViewById(R.id.textView)
      progressBar = view!!.findViewById(R.id.download_progress)

      // check if there are ads already fetched and if stale
      // otherwise refresh the adverts from api
      launch {
         val adverts = viewModel.getAdverts()
         if (adverts.isEmpty() || viewModel.isStale()) {
            viewModel.fetchAdverts()
            textView.text = "Downloading ..."
         } else {
            viewModel.setPlayableAdverts(adverts)
            findNavController().navigate(R.id.destination_player)
         }
      }
   }

   override fun onResume() {
      super.onResume()

      viewModel.downloadedCount.observe(this, Observer {
         if (!isSetprogressBarSize) {
            advertsCount = viewModel.getAdvertsSize() * 100
            isSetprogressBarSize = true
         }
         setProgressBarSize(it)
      })
      viewModel.hasDownloaded.observe(this, Observer {
         if (it) {
            findNavController().navigate(R.id.destination_player)
         }
      })
   }

   private fun setProgressBarSize(count: Int) {
      progressBar.progress = (count / advertsCount) * 100
   }
}