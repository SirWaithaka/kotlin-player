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
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class LocationsFragment : ScopedFragment(), KodeinAware {
   companion object {
      const val TAG = "LocationsFragment"
   }

   override val kodein: Kodein by kodein()
   private val viewModelFactory: LocationsVMFactory by instance()
   private lateinit var viewModel: LocationsViewModel

   // select drop down for locations
   private lateinit var locationsSpinner: Spinner
   private lateinit var adapter: ArrayAdapter<Location>

   override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
      return inflater.inflate(R.layout.fragment_locations, container, false)
   }

   override fun onActivityCreated(savedInstanceState: Bundle?) {
      super.onActivityCreated(savedInstanceState)

      Log.d(TAG, "LocationsFragment: onActivityCreated")
      viewModel = ViewModelProviders.of(activity!!, viewModelFactory).get(LocationsViewModel::class.java)
      adapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, ArrayList<Location>())
      locationsSpinner = view!!.findViewById(R.id.spinner_locations)
      adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
      locationsSpinner.onItemSelectedListener = SpinnerItemSelectedListener
   }

   override fun onResume() {
      super.onResume()
      Log.d(TAG, "LocationsFragment: onResume")
      val locationsLiveData = viewModel.mLocations
      locationsLiveData.observe(this, Observer {
         viewModel.locationFragmentCalling()
         if (it == null) {
            Log.d(TAG, "No data returned")
            return@Observer
         }

         Log.d(TAG, "Returned: $it")
         adapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, it)
         locationsSpinner.adapter = adapter
      })
   }
}

private fun selectedLocation(location: Location) {
   Log.d(TAG, "Selected ${location.placeName}")
}

object SpinnerItemSelectedListener : AdapterView.OnItemSelectedListener {
   override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
      val location = parent.selectedItem as Location
      selectedLocation(location)
   }

   override fun onNothingSelected(parent: AdapterView<*>?) {
      Log.d(TAG, "Not selected anything")
   }
}