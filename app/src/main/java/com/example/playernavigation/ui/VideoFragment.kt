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

class VideoFragment : Fragment() {
   private val TAG = "VideoFragment"

   override fun onAttach(context: Context?) {
      super.onAttach(context)
      Log.d(TAG, "onAttach: video fragment")
   }

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      Log.d(TAG, "onCreate: video fragment")
   }

   override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
      return inflater.inflate(R.layout.fragment_video, container, false)
   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)
      Log.d(TAG, "onViewCreated: video fragment")
   }

   override fun onActivityCreated(savedInstanceState: Bundle?) {
      super.onActivityCreated(savedInstanceState)
      Log.d(TAG, "onActivityCreated: video fragment")
   }

   override fun onResume() {
      super.onResume()
      Log.d(TAG, "onResume: video fragment")

      val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
      with (sharedPref.edit()) {
         putString("MEDIA", "image")
         apply()
      }

      Handler().postDelayed({
         Log.d(TAG, "image timeout")
         this.findNavController().popBackStack()
      }, 10000)
   }

   override fun onPause() {
      super.onPause()
      Log.d(TAG, "onPause: video fragment")
   }

   override fun onStop() {
      super.onStop()
      Log.d(TAG, "onStop: video fragment")
   }

   override fun onDestroyView() {
      super.onDestroyView()
      Log.d(TAG, "onDestroyView: video fragment")
   }

   override fun onDestroy() {
      super.onDestroy()
      Log.d(TAG, "onDestroy: video fragment")
   }

   override fun onDetach() {
      super.onDetach()
      Log.d(TAG, "onDetach: video fragment")
   }
}