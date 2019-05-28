package com.youtise.player.ui.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlin.coroutines.CoroutineContext

abstract class ScopedViewModel: ViewModel(), CoroutineScope {

   protected val job = Job()
   override val coroutineContext: CoroutineContext
      get() = job + Dispatchers.IO

   override fun onCleared() {
      super.onCleared()
      coroutineContext.cancelChildren()
      job.cancel()
   }
}