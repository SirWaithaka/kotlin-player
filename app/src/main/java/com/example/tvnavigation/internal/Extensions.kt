package com.example.tvnavigation.internal

import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.EditText
import com.example.tvnavigation.data.db.entities.Advert

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
   val mediaPath = Environment
      .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
      .toString()

   val stringBuilder = StringBuilder(mediaPath).append("/")
   return stringBuilder.append(this.fileName).toString()
}