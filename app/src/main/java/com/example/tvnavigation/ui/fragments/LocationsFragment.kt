package com.example.tvnavigation.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.tvnavigation.R
import com.example.tvnavigation.data.db.entities.Location
import com.example.tvnavigation.ui.base.ScopedFragment
import com.example.tvnavigation.ui.viewmodels.LocationsVMFactory
import com.example.tvnavigation.ui.viewmodels.LocationsViewModel
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class LocationsFragment: ScopedFragment(), KodeinAware {
   val TAG = "LocationsFragment"
   
   override val kodein: Kodein by kodein()
   private val viewModelFactory: LocationsVMFactory by instance()
   private lateinit var viewModel: LocationsViewModel

   // select drop down for locations
   private lateinit var locationsSpinner: Spinner

   override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
      return inflater.inflate(R.layout.fragment_locations, container, false)

   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)

      locationsSpinner = view.findViewById(R.id.spinner_locations)
   }

   override fun onActivityCreated(savedInstanceState: Bundle?) {
      super.onActivityCreated(savedInstanceState)

      val locations: MutableList<Location> = ArrayList()
      locations.add(Location("select_location", "Select Location", "No Password"))

      val propContext = context

      viewModel = ViewModelProviders.of(this, viewModelFactory).get(LocationsViewModel::class.java)
      launch {
         val locationsLiveData = viewModel.locations.await()
         locationsLiveData.observe(this@LocationsFragment, Observer {
            if (it == null) return@Observer

            val adapter = ArrayAdapter<Location>(propContext!!, android.R.layout.simple_spinner_item, it)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            locationsSpinner.adapter = adapter
         })
      }

      locationsSpinner.onItemSelectedListener = SpinnerItemSelectedListener
      }
   }

   private fun selectedLocation(location: Location) {
      Log.d(TAG, "Selected ${location.placeName}")
   }

   object SpinnerItemSelectedListener: AdapterView.OnItemSelectedListener {
      override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
         val location = parent.selectedItem as Location
         selectedLocation(location)
      }

      override fun onNothingSelected(parent: AdapterView<*>?) {
         Log.d(TAG, "Not selected anything")
      }
}