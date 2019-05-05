package com.example.tvnavigation.ui.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.tvnavigation.R
import com.example.tvnavigation.ui.base.ScopedFragment
import com.example.tvnavigation.ui.viewmodels.AdvertsViewModel
import com.example.tvnavigation.ui.viewmodels.LocationsViewModel
import com.example.tvnavigation.ui.viewmodels.ViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class HomeFragment: ScopedFragment(), KodeinAware {
   private val TAG = "HomeFragment"
   override val kodein: Kodein by kodein()
   private val viewModelFactory: ViewModelFactory by instance()
   private lateinit var adViewModel: AdvertsViewModel
   private lateinit var locViewModel: LocationsViewModel
   private val appPermissions = listOf(
      Manifest.permission.WRITE_EXTERNAL_STORAGE,
      Manifest.permission.READ_PHONE_STATE
   )
   private val permissionsRequestCode: Int = 1240


   override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
      return inflater.inflate(R.layout.fragment_home, container, false)
   }

   override fun onActivityCreated(savedInstanceState: Bundle?) {
      super.onActivityCreated(savedInstanceState)
      adViewModel = ViewModelProviders.of(activity!!, viewModelFactory).get(AdvertsViewModel::class.java)
      locViewModel = ViewModelProviders.of(activity!!, viewModelFactory).get(LocationsViewModel::class.java)
   }

   override fun onStart() {
      super.onStart()

      if(checkAndRequestPermissions(activity!!)) {
         launchApp()
      }
   }

   override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
      if (requestCode == permissionsRequestCode) {
         val deniedPermissions = hashMapOf<String, Int>()
         var deniedCount = 0

         // Gather permission grant result
         for ((index, result) in grantResults.withIndex()) {
            // Add permissions which are only denied
            if (result == PackageManager.PERMISSION_DENIED) {
               deniedPermissions[permissions[index]] = result
               deniedCount += 1
            }
         }

         // Check if all permissions were granted
         if (deniedCount == 0)
            launchApp()
         else {
            TODO("Implement Logic When app is denied permissions")
         }
      }
   }

   private fun launchApp() {
      // behind the scenes check if app already is authenticated
      val hasAuthenticated = locViewModel.getAuthenticationStatus()
      Log.d(TAG, "checking auth status: $hasAuthenticated")
      if (hasAuthenticated) this@HomeFragment.findNavController().navigate(R.id.destination_downloader)
      else this@HomeFragment.findNavController().navigate(R.id.destination_email)
   }

   private fun checkAndRequestPermissions(activity: FragmentActivity): Boolean {
      val listOfPermissionsNeeded = mutableListOf<String>()

      // check which permissions are granted
      for(perm in appPermissions) {
         if (ContextCompat.checkSelfPermission(activity, perm) != PackageManager.PERMISSION_GRANTED)
            listOfPermissionsNeeded.add(perm)
      }

      // ask for non-granted permissions
      if(listOfPermissionsNeeded.isNotEmpty()) {
         requestPermissions(listOfPermissionsNeeded.toTypedArray(), permissionsRequestCode)
         return false
      }
      return true
   }
}