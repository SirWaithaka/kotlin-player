package com.example.playernavigation.ui

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playernavigation.R

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
   }

   override fun onStart() {
      super.onStart()
   }

   override fun onResume() {
      super.onResume()
      Handler().postDelayed({
         Log.d(TAG, "Timeout")
         this.findNavController().navigate(R.id.destination_image)
      }, 10000)
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
      Log.d(TAG, "onDestroy: player fragment")
   }
}