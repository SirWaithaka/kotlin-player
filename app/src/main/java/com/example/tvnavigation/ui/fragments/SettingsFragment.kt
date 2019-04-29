package com.example.tvnavigation.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tvnavigation.R
import com.example.tvnavigation.ui.adapters.SettingsRecyclerViewAdapter
import com.example.tvnavigation.ui.base.ScopedFragment

class SettingsFragment : ScopedFragment() {

   private val TAG = "SettingsFragment"

   override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
      return inflater.inflate(R.layout.fragment_settings, container, false)
   }

   override fun onActivityCreated(savedInstanceState: Bundle?) {
      super.onActivityCreated(savedInstanceState)

      Log.d(TAG, "onActivityCreated")

      val settingsDataList = hashMapOf(
         Pair("Registered Email", "info@somethinginc.com"),
         Pair("Location Name", "Waithaka Location"),
         Pair("Location ID", "93938483900-dummy"),
         Pair("Logout", "Delete session, wipe local data and logout.")
      )

      val recyclerView = view!!.findViewById<RecyclerView>(R.id.settingsList)
      val recyclerViewAdapter = SettingsRecyclerViewAdapter(settingsDataList)
      recyclerView.setHasFixedSize(true)
      recyclerView.adapter = recyclerViewAdapter
      recyclerView.layoutManager = LinearLayoutManager(context)
   }
}