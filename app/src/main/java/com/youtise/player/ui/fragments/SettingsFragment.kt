package com.youtise.player.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.youtise.player.R
import com.youtise.player.ui.adapters.SettingsRecyclerViewAdapter
import com.youtise.player.ui.viewmodels.SettingsViewModel
import com.youtise.player.ui.viewmodels.ViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class SettingsFragment : Fragment(), KodeinAware {

//   private val TAG = "SettingsFragment"
   override val kodein: Kodein by kodein()

   private lateinit var recyclerViewAdapter: SettingsRecyclerViewAdapter
   private val viewModelFactory: ViewModelFactory by instance()
   private lateinit var viewModel: SettingsViewModel
   private lateinit var settingsInformation: SettingsViewModel.PlayerInformation
   private val recyclerView by lazy { view!!.findViewById<RecyclerView>(R.id.settingsList) }

   override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
      return inflater.inflate(R.layout.fragment_settings, container, false)
   }

   override fun onActivityCreated(savedInstanceState: Bundle?) {
      super.onActivityCreated(savedInstanceState)
      viewModel = ViewModelProviders.of(this, viewModelFactory).get(SettingsViewModel::class.java)
      settingsInformation = viewModel.getPlayerInformation()

      recyclerViewAdapter = SettingsRecyclerViewAdapter()
      recyclerView.setHasFixedSize(true)
      recyclerView.layoutManager = LinearLayoutManager(context)
   }

   override fun onResume() {
      super.onResume()

      val settingsDataList = hashMapOf(
         Pair("Registered Email", settingsInformation.email),
         Pair("Location Name", settingsInformation.locationName),
         Pair("Player ID", settingsInformation.playerId)
      )
      recyclerViewAdapter.setInformationData(settingsDataList)
      recyclerView.adapter = recyclerViewAdapter
   }
}