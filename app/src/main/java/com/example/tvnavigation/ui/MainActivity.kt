package com.example.tvnavigation.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.tvnavigation.R
import com.example.tvnavigation.ui.viewmodels.ErrorsViewModel
import com.example.tvnavigation.ui.viewmodels.ViewModelFactory
import com.google.android.material.navigation.NavigationView
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
   private lateinit var drawerNavView: NavigationView

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      setContentView(R.layout.activity_main)
      viewModel = ViewModelProviders.of(this, viewModelFactory).get(ErrorsViewModel::class.java)
      drawerNavView = findViewById(R.id.nav_view)
      drawerNavView.setNavigationItemSelectedListener(DrawerNavActionListener)
      val navController = findNavController(R.id.nav_host_fragment)
      findViewById<NavigationView>(R.id.nav_view).setupWithNavController(navController)
   }

   override fun onResume() {
      super.onResume()
      viewModel.httpErrorResponse.observe(this, Observer {
         Alerter.create(this)
            .setTitle("Something Wrong!")
            .setText(it)
            .setDuration(10000)
            .setBackgroundColorRes(R.color.material_deep_teal_200)
            .show()
      })
   }

   private object DrawerNavActionListener: NavigationView.OnNavigationItemSelectedListener {
      override fun onNavigationItemSelected(item: MenuItem): Boolean {
         when(item.itemId) {
            R.id.nav_advert_playlist -> ""
            R.id.nav_refresh -> ""
            R.id.nav_settings -> ""
            R.id.nav_factory_reset -> ""
         }
         return true
      }
   }
}
