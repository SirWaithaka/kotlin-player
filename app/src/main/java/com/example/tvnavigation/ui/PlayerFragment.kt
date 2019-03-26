package com.example.tvnavigation.ui

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
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

const val TAG = "PlayerFragment"

class PlayerFragment : ScopedFragment(), KodeinAware {

   override val kodein: Kodein by kodein()
   private val viewModelFactory: LocationsVMFactory by instance()
   private lateinit var viewModel: LocationsViewModel

   override fun onAttach(context: Context?) {
      super.onAttach(context)

      Log.d(TAG, "fragment on attach method called")
   }

   override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
      return inflater.inflate(R.layout.fragment_player, container, false)
   }

   override fun onActivityCreated(savedInstanceState: Bundle?) {
      super.onActivityCreated(savedInstanceState)
      Log.d(TAG, "Fragment on activity created")

      viewModel = ViewModelProviders.of(this, viewModelFactory).get(LocationsViewModel::class.java)
      launch {
         val locations = viewModel.locations.await()
         locations.observe(this@PlayerFragment, Observer {
            if (it == null) return@Observer

            player_textView.text = it.toString()
         })
      }
   }

   override fun onStart() {
      super.onStart()
      Log.d(TAG, "onStart: Player Fragment onStart")
   }

//   override fun onResume() {
//      super.onResume()
//      val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
//      val defaultValue = "image"
//      val highScore = sharedPref.getString("MEDIA", defaultValue)

//      when (highScore) {
//         "image" -> this.findNavController().navigate(R.id.destination_image)
//         "video" -> this.findNavController().navigate(R.id.destination_video)
//      }
//   }

   override fun onPause() {
      super.onPause()

      Log.d(TAG, "onPause: Player Fragement paused")
   }

   override fun onStop() {
      super.onStop()
      Log.d(TAG, "onStop: Player fragment stopped")
   }

   override fun onDestroyView() {
      super.onDestroyView()
      Log.d(TAG, "onDestroyView: player fragment")
   }

   override fun onDestroy() {
      super.onDestroy()
      Log.d(TAG, "onDestroy: player fragment")
   }

   override fun onDetach() {
      super.onDetach()
      Log.d(TAG, "onDetach: player fragment")
   }
}