package com.example.tvnavigation.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.tvnavigation.R
import com.example.tvnavigation.ui.base.ScopedFragment
import com.example.tvnavigation.ui.viewmodels.AdvertsViewModel
import com.example.tvnavigation.ui.viewmodels.LocationsViewModel
import com.example.tvnavigation.ui.viewmodels.ViewModelFactory
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class HomeFragment: ScopedFragment(), KodeinAware {
   private val TAG = "HomeFragment"
   override val kodein: Kodein by kodein()
   private val viewModelFactory: ViewModelFactory by instance()
   private lateinit var adViewModel: AdvertsViewModel
   private lateinit var locViewModel: LocationsViewModel


   override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
      return inflater.inflate(R.layout.fragment_home, container, false)
   }

   override fun onActivityCreated(savedInstanceState: Bundle?) {
      super.onActivityCreated(savedInstanceState)
      adViewModel = ViewModelProviders.of(activity!!, viewModelFactory).get(AdvertsViewModel::class.java)
      locViewModel = ViewModelProviders.of(activity!!, viewModelFactory).get(LocationsViewModel::class.java)
   }

   override fun onResume() {
      super.onResume()
      Log.d(TAG, "HomeFragment: onResume")
//      adViewModel.hasDownloadedAdverts.observe(this, Observer {
//         if (it) this.findNavController().navigate(R.id.destination_downloader)
//         else this.findNavController().navigate(R.id.destination_player)
//      })
//
//      locViewModel.isAuthenticated.observe(this, Observer {
//         Log.d(TAG, "Home Observer: $it")
//         if (it) this.findNavController().navigate(R.id.destination_downloader)
//         else this.findNavController().navigate(R.id.destination_email)
//      })
   }
   override fun onStart() {
      super.onStart()

      // behind the scenes check if app already is authenticated
      launch {
         val hasAuthenticated = locViewModel.getAuthenticationStatus()
         locViewModel.setAuthenticationStatus(hasAuthenticated)
         if (hasAuthenticated) this@HomeFragment.findNavController().navigate(R.id.destination_downloader)
         else this@HomeFragment.findNavController().navigate(R.id.destination_email)
      }
   }
}