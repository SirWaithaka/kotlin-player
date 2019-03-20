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
import com.example.playernavigation.Samples
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util

class VideoFragment : Fragment() {
   private val TAG = "VideoFragment"

   private lateinit var player: SimpleExoPlayer
   private lateinit var playerView: PlayerView

   override fun onAttach(context: Context?) {
      super.onAttach(context)
      Log.d(TAG, "onAttach: video fragment")
   }

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      Log.d(TAG, "onCreate: video fragment")
   }

   override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
      val videoFragmentView = inflater.inflate(R.layout.fragment_video, container, false)
//      playerView = videoFragmentView.findViewById(R.id.video_view)
      return videoFragmentView
   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)

      playerView = view.findViewById(R.id.video_view)
      Log.d(TAG, "onViewCreated: video fragment")
   }

   override fun onActivityCreated(savedInstanceState: Bundle?) {
      super.onActivityCreated(savedInstanceState)
      Log.d(TAG, "onActivityCreated: video fragment")
   }

   override fun onStart() {
      super.onStart()
      Log.d(TAG, "onStart: video fragment")

      player = ExoPlayerFactory.newSimpleInstance(this.context, DefaultTrackSelector())

      playerView.player = player
      val sample = Samples()
      val dataSourceFactory = DefaultDataSourceFactory(this.context, Util.getUserAgent(this.context, "youtise-player"))
      val mediaSource = ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(sample.MEDIA_URI)

      player.prepare(mediaSource)

      player.addListener(PlayBackStateChanged(this))
      player.playWhenReady = true
   }

   override fun onResume() {
      super.onResume()
      Log.d(TAG, "onResume: video fragment")

      val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
      with (sharedPref.edit()) {
         putString("MEDIA", "image")
         apply()
      }

      /*Handler().postDelayed({
         Log.d(TAG, "video timeout")
         this.findNavController().popBackStack()
      }, 10000)*/
   }

   override fun onPause() {
      super.onPause()
      Log.d(TAG, "onPause: video fragment")
   }

   override fun onStop() {
      super.onStop()
      Log.d(TAG, "onStop: video fragment")

      playerView.player = null
      player.release()
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

   inner class PlayBackStateChanged (val fragment: Fragment): Player.EventListener {
      override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
         when (playbackState) {
            Player.STATE_ENDED -> {
               Log.d(TAG, "video has finished playing")

               fragment.findNavController().popBackStack()
            }
            else -> super.onPlayerStateChanged(playWhenReady, playbackState)
         }
      }
   }
}