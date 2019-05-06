package com.example.player.ui

import android.os.Bundle
import android.view.KeyEvent
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.player.R
import com.example.player.ui.viewmodels.ErrorsViewModel
import com.example.player.ui.viewmodels.SettingsViewModel
import com.example.player.ui.viewmodels.ViewModelFactory
import com.google.android.material.navigation.NavigationView
import com.tapadoo.alerter.Alerter
import kotlinx.android.synthetic.main.activity_main.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class MainActivity : AppCompatActivity(), KodeinAware {

//   private val TAG = "PlayerMainActivity"

   override val kodein: Kodein by kodein()
   private val viewModelFactory: ViewModelFactory by instance()
   private lateinit var errorsViewModel: ErrorsViewModel
   private lateinit var settingsViewModel: SettingsViewModel
   private lateinit var drawerNavView: NavigationView
   private val navController by lazy { findNavController(R.id.nav_host_fragment) }

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      setContentView(R.layout.activity_main)
      errorsViewModel = ViewModelProviders.of(this, viewModelFactory).get(ErrorsViewModel::class.java)
      settingsViewModel = ViewModelProviders.of(this, viewModelFactory).get(SettingsViewModel::class.java)

      // setup Drawer Layout with NavigationController
      drawerNavView = findViewById(R.id.nav_view)
      drawerNavView.setupWithNavController(navController)
      drawerNavView.setNavigationItemSelectedListener(DrawerNavActionListener())
   }

   override fun onResume() {
      super.onResume()
      errorsViewModel.httpErrorResponse.observe(this, Observer {
         Alerter.create(this)
            .setTitle("Something Wrong!")
            .setText(it)
            .setDuration(10000)
            .setBackgroundColorRes(R.color.app_accent)
            .show()
      })
   }

   override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {

      if (keyCode != KeyEvent.KEYCODE_DPAD_UP) return super.onKeyUp(keyCode, event)
      if (!drawer_layout.isDrawerOpen(GravityCompat.START)) drawer_layout.openDrawer(GravityCompat.START)
      return true
   }

   override fun onBackPressed() {
      if (!drawer_layout.isDrawerOpen(GravityCompat.START)) super.onBackPressed()
      drawer_layout.closeDrawer(GravityCompat.START)
   }

   override fun onSupportNavigateUp() =
      Navigation.findNavController(this, R.id.nav_host_fragment).navigateUp() ||
      super.onSupportNavigateUp()

   private fun handleDrawerActions(resourceId: Int) {

      when(resourceId) {
         R.id.nav_refresh -> {
            settingsViewModel.invalidateAdverts()
            navController.navigate(R.id.destination_downloader)
         }
         R.id.nav_logout -> {
            settingsViewModel.invalidateSession()
            navController.navigate(R.id.destination_home)
         }
      }
   }

   private inner class DrawerNavActionListener: NavigationView.OnNavigationItemSelectedListener {
      override fun onNavigationItemSelected(item: MenuItem): Boolean {
         when(item.itemId) {
            R.id.nav_advert_playlist -> navController.navigate(R.id.destination_playlist)
            R.id.nav_player -> navController.navigate(R.id.destination_home)
            R.id.nav_settings -> navController.navigate(R.id.destination_settings)
            else -> handleDrawerActions(item.itemId)
         }
         drawer_layout.closeDrawer(GravityCompat.START)
         return true
      }
   }
}