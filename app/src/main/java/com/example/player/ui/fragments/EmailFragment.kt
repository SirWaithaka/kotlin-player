package com.example.player.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.player.R
import com.example.player.internal.isValidEmail
import com.example.player.internal.onTextChanged
import com.example.player.ui.viewmodels.LocationsViewModel
import com.example.player.ui.viewmodels.ViewModelFactory
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class EmailFragment : Fragment(), KodeinAware {
   companion object {
//      const val TAG = "EmailFragment"
   }

   override val kodein: Kodein by kodein()
   private val viewModelFactory: ViewModelFactory by instance()

   private lateinit var emailInput: TextInputEditText
   private lateinit var submitButton: MaterialButton
   private lateinit var viewModel: LocationsViewModel

   private val onEmailSubmitListener = View.OnClickListener {
      val userEmail: String = emailInput.text.toString()
      viewModel.validateEmail(userEmail)
      this.findNavController().navigate(R.id.destination_locations)
   }
   override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
      return inflater.inflate(R.layout.fragment_email, container, false)
   }

   override fun onActivityCreated(savedInstanceState: Bundle?) {
      super.onActivityCreated(savedInstanceState)
      viewModel = ViewModelProviders.of(activity!!, viewModelFactory).get(LocationsViewModel::class.java)
   }
   override fun onResume() {
      super.onResume()

      emailInput = view!!.findViewById(R.id.textInput_email)
      // onTextChanged is a custom extension to TextInput
      emailInput.onTextChanged {
         val userEmail = it
         submitButton.isEnabled = userEmail.isValidEmail()
      }
      submitButton = view!!.findViewById(R.id.mButton_email)
      submitButton.setOnClickListener(onEmailSubmitListener)
   }
}