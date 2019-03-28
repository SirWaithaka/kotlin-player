package com.example.tvnavigation.ui.fragments

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tvnavigation.R

class ImageFragment : Fragment() {
   companion object {
      const val TAG = "ImageFragment"
   }

   override fun onAttach(context: Context?) {
      super.onAttach(context)
      Log.d(TAG, "onAttach: image fragment")
   }

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      Log.d(TAG, "onCreate: image fragment")
   }

   override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
      Log.d(TAG, "onCreateView: image fragment")
      return inflater.inflate(R.layout.fragment_image, container, false)
   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)
      Log.d(TAG, "onViewCreated: image fragment")

//      val sample = Samples()
      val imageView = view.findViewById<ImageView>(R.id.image_view)
      imageView.setImageResource(R.drawable.hugs)
   }

   override fun onResume() {
      super.onResume()
      Log.d(TAG, "onResume: image fragment")

      val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
      with (sharedPref.edit()) {
         putString("MEDIA", "video")
         apply()
      }

      Handler().postDelayed({
         Log.d(TAG, "image timeout")
         this.findNavController().popBackStack()
      }, 10000)
   }

   override fun onPause() {
      super.onPause()
      Log.d(TAG, "onPause: image fragment")
   }

   override fun onStop() {
      super.onStop()
      Log.d(TAG, "onStop: image fragment stopped")
   }

   override fun onDestroyView() {
      super.onDestroyView()
      Log.d(TAG, "onDestroyView: image fragment")
   }

   override fun onDestroy() {
      super.onDestroy()
      Log.d(TAG, "onDestroy: image fragment")
   }

   override fun onDetach() {
      super.onDetach()
      Log.d(TAG, "onDetach: image fragment")
   }
}