package com.youtise.player.internal

import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.EditText
import com.youtise.player.data.db.entities.Advert
import kotlinx.coroutines.*

// STRING EXTENSIONS
fun String.isValidEmail(): Boolean {
   return this.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

// VIEW EXTENSIONS
fun EditText.onTextChanged(onTextChanged: (String) -> Unit) {
   this.addTextChangedListener(object: TextWatcher {
      override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
      override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
         onTextChanged.invoke(s.toString())
      }
      override fun afterTextChanged(s: Editable) { }
   })
}

fun Advert.getLocalMediaPath(): String {
   val mediaPath = DOWNLOADS_DIR

   val stringBuilder = StringBuilder(mediaPath).append("/")
   return stringBuilder.append(this.fileName).toString()
}

fun looperThread(
   start: Boolean = true,
   isDaemon: Boolean = false,
   contextClassLoader: ClassLoader? = null,
   name: String? = null,
   priority: Int = -1,
   block: () -> Unit
): Thread {
   val thread = object : Thread() {
      override fun run() {
         Looper.prepare()
         block()
         Looper.loop()
      }
   }
   if (isDaemon)
      thread.isDaemon = true
   if (priority > 0)
      thread.priority = priority
   if (name != null)
      thread.name = name
   if (contextClassLoader != null)
      thread.contextClassLoader = contextClassLoader
   if (start)
      thread.start()
   return thread
}

fun looperCoroutine(block: () -> Unit): Job = runBlocking {
   return@runBlocking launch {
      Looper.prepare()
      block()
      Looper.loop()
   }
}