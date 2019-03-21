package com.example.tvnavigation

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {

   private val TAG = "PlayerMainActivity"

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      setContentView(R.layout.activity_main)
      Log.d(TAG, "Activity on create here")

      val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return
      with (sharedPref.edit()) {
         putString("MEDIA", "image")
         commit()
      }
   }

   override fun onResume() {
      super.onResume()
      Log.d(TAG, "Resume state called")
   }
}
