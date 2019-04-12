package com.example.tvnavigation.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.tvnavigation.R
import com.example.tvnavigation.ui.viewmodels.ErrorsViewModel
import com.example.tvnavigation.ui.viewmodels.ViewModelFactory
import com.tapadoo.alerter.Alerter
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class MainActivity : AppCompatActivity(), KodeinAware {

//   private val TAG = "PlayerMainActivity"

   override val kodein: Kodein by kodein()
   private val viewModelFactory: ViewModelFactory by instance()
   private lateinit var viewModel: ErrorsViewModel

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      setContentView(R.layout.activity_main)
      viewModel = ViewModelProviders.of(this, viewModelFactory).get(ErrorsViewModel::class.java)
   }

   override fun onResume() {
      super.onResume()
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
