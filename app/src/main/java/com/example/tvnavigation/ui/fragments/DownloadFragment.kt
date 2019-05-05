package com.example.tvnavigation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.tvnavigation.R
import com.example.tvnavigation.ui.base.ScopedFragment
import com.example.tvnavigation.ui.viewmodels.AdvertsViewModel
import com.example.tvnavigation.ui.viewmodels.ViewModelFactory
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class DownloadFragment: ScopedFragment(), KodeinAware {

//   private val TAG = "DownloadFragment"
   override val kodein: Kodein by kodein()
   private val viewModelFactory: ViewModelFactory by instance()
   private lateinit var viewModel: AdvertsViewModel
   private lateinit var textView: TextView
   private lateinit var downloadCountTextView: TextView
   private lateinit var progressBar: ProgressBar

   override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
      return inflater.inflate(R.layout.fragment_download, container, false)
   }

   override fun onActivityCreated(savedInstanceState: Bundle?) {
      super.onActivityCreated(savedInstanceState)

      viewModel = ViewModelProviders.of(activity!!, viewModelFactory).get(AdvertsViewModel::class.java)
      textView = view!!.findViewById(R.id.textView)
      downloadCountTextView = view!!.findViewById(R.id.download_count)
      progressBar = view!!.findViewById(R.id.download_progress)

      // check if there are ads already fetched and if stale
      // otherwise refresh the adverts from api
      launch {
         val adverts = viewModel.getAdverts()
         if (adverts.isEmpty() || viewModel.isStale()) {
            viewModel.fetchAdverts()
            textView.text = getString(R.string.downloading)
         } else {
            viewModel.setPlayableAdverts(adverts)
            findNavController().navigate(R.id.destination_player)
         }
      }
   }

   override fun onResume() {
      super.onResume()

      val liveData = viewModel.getDownloadInfo()

      liveData.observe(this, Observer<AdvertsViewModel.MergedData> {
         when (it) {
            is AdvertsViewModel.MergedData.HasDownloadedStatus -> {
               if (it.hasDownloaded != null)
                  if (it.hasDownloaded) findNavController().navigate(R.id.destination_player)
            }
            is AdvertsViewModel.MergedData.CountInfo -> {
            }
            is AdvertsViewModel.MergedData.DownloadedCount -> {
               downloadCountTextView.text = getString(R.string.download_progress_count, it.count)
            }
         }
      })
   }
}