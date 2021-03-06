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
import com.youtise.player.ui.adapters.PlaylistRecyclerViewAdapter
import com.youtise.player.data.db.models.MediaModel
import com.youtise.player.ui.viewmodels.SettingsViewModel
import com.youtise.player.ui.viewmodels.ViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class PlaylistFragment: Fragment(), KodeinAware {

   override val kodein: Kodein by kodein()
   private val viewModelFactory: ViewModelFactory by instance()
   private lateinit var viewModel: SettingsViewModel
   private lateinit var mediaPlaylist: List<MediaModel>
   private val recyclerView by lazy { view!!.findViewById<RecyclerView>(R.id.playlistRecyclerView) }

   override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
      return inflater.inflate(R.layout.fragment_playlist, container, false)
   }

   override fun onActivityCreated(savedInstanceState: Bundle?) {
      super.onActivityCreated(savedInstanceState)

      viewModel = ViewModelProviders.of(activity!!, viewModelFactory).get(SettingsViewModel::class.java)
      mediaPlaylist = viewModel.getCurrentPlaylist()
      val recyclerViewAdapter = PlaylistRecyclerViewAdapter(mediaPlaylist)

      recyclerView.adapter = recyclerViewAdapter
//      recyclerView.setHasFixedSize(true)
      recyclerView.layoutManager = LinearLayoutManager(context)
   }
}