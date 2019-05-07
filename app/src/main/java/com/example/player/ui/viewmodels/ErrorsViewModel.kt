package com.example.player.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.player.data.network.ErrorsHandler


/**
 * Provides a single view of errors generated in the application
 * and need to be shown to the user.
 */
class ErrorsViewModel(errorsHandler: ErrorsHandler): ViewModel() {

   val httpErrorResponse: LiveData<String> = errorsHandler.getHttpErrorResponses()

}