package com.example.player.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.player.R
import com.example.player.data.db.entities.Location
import com.example.player.internal.onTextChanged
import com.example.player.ui.viewmodels.LocationsViewModel
import com.example.player.ui.viewmodels.ViewModelFactory
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.fragment_locations.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class LocationsFragment : Fragment(), KodeinAware {

   private val TAG = "LocationsFragment"

   override val kodein: Kodein by kodein()
   private val viewModelFactory: ViewModelFactory by instance()

   private var hasSelectedLocation: Boolean = false
   private val credentials: Credentials = Credentials()
   private lateinit var viewModel: LocationsViewModel

   // select drop down for locations
   private lateinit var locationsSpinner: Spinner
   private lateinit var adapter: ArrayAdapter<Location>
   private lateinit var passwordInput: TextInputEditText
   private lateinit var loginButton: MaterialButton

   override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
      return inflater.inflate(R.layout.fragment_locations, container, false)
   }

   override fun onActivityCreated(savedInstanceState: Bundle?) {
      super.onActivityCreated(savedInstanceState)

      viewModel = ViewModelProviders.of(activity!!, viewModelFactory).get(LocationsViewModel::class.java)

      adapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, ArrayList<Location>())
      locationsSpinner = view!!.findViewById(R.id.spinner_locations)

      adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
      locationsSpinner.onItemSelectedListener = SpinnerItemSelectedListener()

      passwordInput = view!!.findViewById(R.id.text_input_password)
      loginButton = view!!.findViewById(R.id.mButton_login)
   }

   override fun onResume() {
      super.onResume()
      val locationsLiveData = viewModel.locations
      locationsLiveData.observe(this, Observer {

         if (it == null)
            return@Observer

         adapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, it)
         locationsSpinner.adapter = adapter
      })
      viewModel.hasAuthenticated.observe(this, Observer {

         if (it.getContent() == true)
            this.findNavController().navigate(R.id.action_back_to_home)

      })
      passwordInput.onTextChanged {
         credentials.password = it
         loginButton.isEnabled = it.isNotEmpty() && hasSelectedLocation
         loginButton.text = getString(R.string.login)
      }
      mButton_login.setOnClickListener(OnCredentialsSubmitListener())
   }


   private class Credentials(
      var location: Location? = null,
      var password: String = ""
   )



   inner class OnCredentialsSubmitListener: View.OnClickListener {
      override fun onClick(v : View) {
         viewModel.validateCredentials(credentials.location!!, credentials.password)
         v as MaterialButton
         v.isEnabled = false
         v.text = getString(R.string.loading)
      }
   }



   inner class SpinnerItemSelectedListener: AdapterView.OnItemSelectedListener {
      override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
         val location = parent.selectedItem as Location
         hasSelectedLocation = true
         credentials.location = location
      }

      override fun onNothingSelected(parent: AdapterView<*>?) {
//         Log.d(TAG, "Spinner: Nothing selected")
      }
   }
}
