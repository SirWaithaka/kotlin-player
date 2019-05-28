package com.youtise.player.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.youtise.player.R
import com.youtise.player.internal.isValidEmail
import com.youtise.player.internal.onTextChanged
import com.youtise.player.ui.viewmodels.LocationsViewModel
import com.youtise.player.ui.viewmodels.ViewModelFactory
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class EmailFragment : Fragment(), KodeinAware {
   private val TAG = "EmailFragment"

   override val kodein: Kodein by kodein()
   private val viewModelFactory: ViewModelFactory by instance()

   private lateinit var emailInput: TextInputEditText
   private lateinit var submitButton: MaterialButton
   private lateinit var viewModel: LocationsViewModel

   override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
      return inflater.inflate(R.layout.fragment_email, container, false)
   }

   override fun onActivityCreated(savedInstanceState: Bundle?) {
      super.onActivityCreated(savedInstanceState)
      viewModel = ViewModelProviders.of(activity!!, viewModelFactory).get(LocationsViewModel::class.java)

      emailInput = view!!.findViewById(R.id.textInput_email)
      submitButton = view!!.findViewById(R.id.mButton_email)
   }
   override fun onResume() {
      super.onResume()
      // check if there is a value in emailInput and change
      // submit button to enabled
      emailInput.text.apply {
         submitButton.isEnabled = this.toString().isValidEmail()
      }

      // onTextChanged is a custom extension to TextInput
      emailInput.onTextChanged {
         submitButton.text = getString(R.string.submit_email)
         submitButton.isEnabled = it.isValidEmail()
      }
      submitButton.setOnClickListener(OnEmailSubmitListener())
      viewModel.hasValidatedEmail.observe(this, Observer {
         if (it.getContent() == true) findNavController().navigate(R.id.destination_locations)
      })
   }

   /*
    * When submit button is clicked submit value to view model
    * Disable button
    */
   private inner class OnEmailSubmitListener: View.OnClickListener {
      override fun onClick(v: View) {
         val userEmail: String = this@EmailFragment.emailInput.text.toString()
         this@EmailFragment.viewModel.submitEmail(userEmail)
         this@EmailFragment.submitButton.isEnabled = false
         this@EmailFragment.submitButton.text = getString(R.string.loading)
      }
   }
}