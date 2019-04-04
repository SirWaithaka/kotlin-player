package com.example.tvnavigation.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
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

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      Log.d(TAG, ZonedDateTime.now().toLocalDateTime().minusHours(23).toString())
      Log.d(TAG, ZonedDateTime.now().toLocalDateTime().toString())
      Log.d(TAG, ZonedDateTime.now().hour.toString())
   }

   override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
      return inflater.inflate(R.layout.fragment_download, container, false)
   }

   override fun onActivityCreated(savedInstanceState: Bundle?) {
      super.onActivityCreated(savedInstanceState)

      viewModel = ViewModelProviders.of(activity!!, viewModelFactory).get(AdvertsViewModel::class.java)
   }

   override fun onResume() {
      super.onResume()

      launch {
         val adverts = viewModel.getAdverts()
         if (adverts.isEmpty()) {
            Log.d(TAG, "No adverts: $adverts")
            viewModel.fetchAdverts()
         }
      }
   }
}