package com.example.player.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.player.R
import com.example.player.ui.viewmodels.AdvertsViewModel
import com.example.player.ui.viewmodels.PlayerViewModel
import com.example.player.ui.viewmodels.ViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class PlayerFragment : Fragment(), KodeinAware {
   private val  TAG = "PlayerFragment"
   override val kodein: Kodein by kodein()
   private val viewModelFactory: ViewModelFactory by instance()
   private lateinit var adViewModel: AdvertsViewModel
   private lateinit var playerViewModel: PlayerViewModel
   private val playableMedia by lazy { adViewModel.getPlayableAdverts() }

   override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
      return inflater.inflate(R.layout.fragment_player, container, false)
   }

   override fun onActivityCreated(savedInstanceState: Bundle?) {
      super.onActivityCreated(savedInstanceState)

      adViewModel = ViewModelProviders.of(activity!!, viewModelFactory).get(AdvertsViewModel::class.java)
      playerViewModel = ViewModelProviders.of(activity!!, viewModelFactory).get(PlayerViewModel::class.java)

      playerViewModel.initStack(playableMedia)

      if (playerViewModel.isMediaStackEmpty()) {
         playerViewModel.initStack(playableMedia)
      }

      val mediaToPlay = playerViewModel.getMediaToPlay()
      playerViewModel.setMediaToPlay(mediaToPlay)

      when(mediaToPlay.type.toLowerCase()) {
         "video" -> findNavController().navigate(R.id.destination_video)
         "image" -> findNavController().navigate(R.id.destination_image)
      }
   }
}