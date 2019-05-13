package com.example.player.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.player.R
import com.example.player.internal.CURRENT_TIME
import com.example.player.internal.PLAY_DURATION_MILLIS
import com.example.player.ui.viewmodels.PlayerViewModel
import com.example.player.ui.viewmodels.ViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class ImageFragment : Fragment(), KodeinAware {
//   private val TAG = "ImageFragment"

   override val kodein: Kodein by kodein()
   private val viewModelFactory: ViewModelFactory by instance()
   private lateinit var viewModel: PlayerViewModel

   override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
      return inflater.inflate(R.layout.fragment_image, container, false)
   }

   override fun onActivityCreated(savedInstanceState: Bundle?) {
      super.onActivityCreated(savedInstanceState)

      viewModel = ViewModelProviders.of(activity!!, viewModelFactory).get(PlayerViewModel::class.java)
      val imagePath = viewModel.getMediaToPlayPath()
      val imageView = view!!.findViewById<ImageView>(R.id.image_view)

      Glide.with(this)
         .load(imagePath)
         .into(imageView)

      val startTime = CURRENT_TIME
      viewModel.setStartTime(startTime)
      viewModel.mediaAboutToPlayEvent(activity!!)
   }

   override fun onResume() {
      super.onResume()

      Handler().postDelayed({
         viewModel.setStopTime(now = CURRENT_TIME)
         viewModel.mediaHasPlayedEvent()
         this.findNavController().popBackStack()
      }, PLAY_DURATION_MILLIS)
   }
}