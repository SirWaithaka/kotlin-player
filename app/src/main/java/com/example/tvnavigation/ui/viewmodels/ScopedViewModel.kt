package com.example.tvnavigation.ui.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class ScopedViewModel: ViewModel(), CoroutineScope {

   private val job = Job()
   override val coroutineContext: CoroutineContext
      get() = job + Dispatchers.IO

   override fun onCleared() {
      super.onCleared()
      job.cancel()
   }
}