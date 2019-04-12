package com.example.tvnavigation.ui.fragments

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.tvnavigation.R
import com.example.tvnavigation.ui.viewmodels.PlayerViewModel
import com.example.tvnavigation.ui.viewmodels.ViewModelFactory
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

class VideoFragment : Fragment(), KodeinAware {
   private val TAG = "VideoFragment"

   override val kodein: Kodein by kodein()
   private val zonedId = ZoneId.systemDefault()

   private lateinit var player: SimpleExoPlayer
   private lateinit var playerView: PlayerView
   private lateinit var mediaPath: Uri
   private lateinit var viewModel: PlayerViewModel
   private val viewModelFactory: ViewModelFactory by instance()


   override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
      return inflater.inflate(R.layout.fragment_video, container, false)
   }

   override fun onActivityCreated(savedInstanceState: Bundle?) {
      super.onActivityCreated(savedInstanceState)

      playerView = view!!.findViewById(R.id.video_view)
      viewModel = ViewModelProviders.of(activity!!, viewModelFactory).get(PlayerViewModel::class.java)
      mediaPath = Uri.parse(viewModel.getMediaToPlayPath())
   }

   override fun onStart() {
      super.onStart()

      player = ExoPlayerFactory.newSimpleInstance(this.context, DefaultTrackSelector())

      playerView.player = player
      val dataSourceFactory = DefaultDataSourceFactory(this.context, Util.getUserAgent(this.context, "youtise-player"))
      val mediaSource = ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(mediaPath)

      player.prepare(mediaSource)

      player.addListener(PlayBackStateChanged(this))
      player.playWhenReady = true

      val startTime = ZonedDateTime.ofInstant(Instant.now(), zonedId)
      viewModel.setStartTime(startTime)
   }

   override fun onStop() {
      super.onStop()

      playerView.player = null
      player.release()
   }

   inner class PlayBackStateChanged (val fragment: Fragment): Player.EventListener {
      override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
         when (playbackState) {
            Player.STATE_ENDED -> {
               // video has finished playing
               viewModel.setStopTime(now = ZonedDateTime.ofInstant(Instant.now(), zonedId))
               viewModel.mediaHasPlayedEvent()
               fragment.findNavController().popBackStack()
            }
            else -> super.onPlayerStateChanged(playWhenReady, playbackState)
         }
      }
   }
}