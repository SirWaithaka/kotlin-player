package com.example.tvnavigation.ui.fragments

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
import com.example.tvnavigation.R
import com.example.tvnavigation.ui.viewmodels.PlayerViewModel
import com.example.tvnavigation.ui.viewmodels.ViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

class ImageFragment : Fragment(), KodeinAware {
//   private val TAG = "ImageFragment"

   private val zonedId = ZoneId.systemDefault()
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

      val startTime = ZonedDateTime.ofInstant(Instant.now(), zonedId)
      viewModel.setStartTime(startTime)
   }

   override fun onResume() {
      super.onResume()

      Handler().postDelayed({
         viewModel.setStopTime(now = ZonedDateTime.ofInstant(Instant.now(), zonedId))
         viewModel.mediaHasPlayedEvent()
         this.findNavController().popBackStack()
      }, 10000)
   }
}