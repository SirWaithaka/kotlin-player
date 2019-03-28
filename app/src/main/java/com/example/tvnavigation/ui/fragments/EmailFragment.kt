package com.example.tvnavigation.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.tvnavigation.R
import com.example.tvnavigation.internal.isValidEmail
import com.example.tvnavigation.internal.onTextChanged
import com.example.tvnavigation.ui.viewmodels.LocationsVMFactory
import com.example.tvnavigation.ui.viewmodels.LocationsViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class EmailFragment : Fragment(), KodeinAware {
   val TAG = "EmailFragment"

   override val kodein: Kodein by kodein()
   private val viewModelFactory: LocationsVMFactory by instance()

   private lateinit var emailInput: TextInputEditText
   lateinit var submitButton: MaterialButton
   private lateinit var viewModel: LocationsViewModel

   private val onEmailSubmitListener = View.OnClickListener {
      val userEmail: String = emailInput.text.toString()
      viewModel.validateEmail(userEmail)
   }
   override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
      return inflater.inflate(R.layout.fragment_email, container, false)
   }

   override fun onActivityCreated(savedInstanceState: Bundle?) {
      super.onActivityCreated(savedInstanceState)
      viewModel = ViewModelProviders.of(this, viewModelFactory).get(LocationsViewModel::class.java)
   }
   override fun onResume() {
      super.onResume()

      emailInput = view!!.findViewById(R.id.textInput_email)
      emailInput.onTextChanged {
         val userEmail = it
         submitButton.isEnabled = userEmail.isValidEmail()
      }
      submitButton = view!!.findViewById(R.id.mButton_email)
      submitButton.setOnClickListener(onEmailSubmitListener)
   }
}