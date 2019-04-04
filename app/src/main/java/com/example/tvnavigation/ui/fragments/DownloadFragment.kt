package com.example.tvnavigation.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tvnavigation.R
import java.time.ZonedDateTime

class DownloadFragment: Fragment() {

   private val TAG = "DownloadFragment"

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      Log.d(TAG, ZonedDateTime.now().hour.toString())
   }

   override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
      return inflater.inflate(R.layout.fragment_download, container, false)
   }
}