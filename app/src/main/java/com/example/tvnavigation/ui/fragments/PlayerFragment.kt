package com.example.tvnavigation.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.tvnavigation.R
import com.example.tvnavigation.ui.base.ScopedFragment
import com.example.tvnavigation.ui.viewmodels.LocationsVMFactory
import com.example.tvnavigation.ui.viewmodels.LocationsViewModel
import kotlinx.android.synthetic.main.fragment_player.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

const val TAG = "PlayerFragment"

class PlayerFragment : ScopedFragment(), KodeinAware {

   override val kodein: Kodein by kodein()
   private val viewModelFactory: LocationsVMFactory by instance()
   private lateinit var viewModel: LocationsViewModel


   override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
      return inflater.inflate(R.layout.fragment_player, container, false)
   }

   override fun onActivityCreated(savedInstanceState: Bundle?) {
      super.onActivityCreated(savedInstanceState)
      Log.d(TAG, "Fragment on activity created")


   }

   override fun onStart() {
      super.onStart()
      Log.d(TAG, "onStart: Player Fragment onStart")
   }

   override fun onResume() {
      super.onResume()

//      when (highScore) {
//         "image" -> this.findNavController().navigate(R.id.destination_image)
//         "video" -> this.findNavController().navigate(R.id.destination_video)
//      }
   }

}