package com.example.tvnavigation.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.tvnavigation.R
import com.example.tvnavigation.data.network.ConnectivityInterceptorImpl
import com.example.tvnavigation.data.network.services.LocationsService
import com.example.tvnavigation.data.repositories.LocationsDataSourceImpl
import com.example.tvnavigation.internal.NoConnectivityException
import kotlinx.android.synthetic.main.fragment_player.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PlayerFragment : Fragment() {
   private val TAG = "PlayerFragment"
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

      val apiService = LocationsService(ConnectivityInterceptorImpl(this.context!!))
      val locationsDataSource = LocationsDataSourceImpl(apiService)

      locationsDataSource.downloadedLocations.observe(this, Observer {
         player_textView.text = it.toString()
      })

      GlobalScope.launch(Dispatchers.Main) {
         locationsDataSource.fetchLocations("kennwaithaka@gmail.com")
      }
   }

   override fun onStart() {
      super.onStart()
      Log.d(TAG, "onStart: Player Fragment onStart")
   }

   override fun onResume() {
      super.onResume()
      val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
      val defaultValue = "image"
      val highScore = sharedPref.getString("MEDIA", defaultValue)

//      when (highScore) {
//         "image" -> this.findNavController().navigate(R.id.destination_image)
//         "video" -> this.findNavController().navigate(R.id.destination_video)
//      }
   }

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