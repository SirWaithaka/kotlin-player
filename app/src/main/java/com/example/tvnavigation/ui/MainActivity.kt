package com.example.tvnavigation.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.tvnavigation.R
import com.example.tvnavigation.ui.viewmodels.LocationsVMFactory
import com.example.tvnavigation.ui.viewmodels.LocationsViewModel
import com.tapadoo.alerter.Alerter
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class MainActivity : AppCompatActivity(), KodeinAware {

   private val TAG = "PlayerMainActivity"

   override val kodein: Kodein by kodein()
   private val viewModelFactory: LocationsVMFactory by instance()
   private lateinit var viewModel: LocationsViewModel

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      setContentView(R.layout.activity_main)
      Log.d(TAG, "Activity on create here")

      viewModel = ViewModelProviders.of(this, viewModelFactory).get(LocationsViewModel::class.java)
   }

   override fun onResume() {
      super.onResume()
      Log.d(TAG, "Resume state called")
      viewModel.httpErrorResponse.observe(this, Observer {
         Alerter.create(this)
            .setTitle("Something Wrong!")
            .setText(it)
            .setDuration(4000)
            .setBackgroundColorRes(R.color.material_deep_teal_200)
            .show()
      })
   }
}
