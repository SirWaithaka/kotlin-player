package com.example.tvnavigation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.tvnavigation.R
import com.example.tvnavigation.ui.viewmodels.AdvertsViewModel
import com.example.tvnavigation.ui.viewmodels.ViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class PlayerFragment : Fragment(), KodeinAware {
   private val  TAG = "PlayerFragment"
   override val kodein: Kodein by kodein()
   private val viewModelFactory: ViewModelFactory by instance()
   private lateinit var viewModel: AdvertsViewModel
   private val playableMedia by lazy { viewModel.getPlayableAdverts() }

   override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
      return inflater.inflate(R.layout.fragment_player, container, false)
   }

   override fun onActivityCreated(savedInstanceState: Bundle?) {
      super.onActivityCreated(savedInstanceState)

      viewModel = ViewModelProviders.of(activity!!, viewModelFactory).get(AdvertsViewModel::class.java)
   }

   override fun onResume() {
      super.onResume()

   }
}