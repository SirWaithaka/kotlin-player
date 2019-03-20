package com.example.playernavigation

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProviders
import com.example.playernavigation.ui.MediaViewModel

class MainActivity : AppCompatActivity() {

   private val TAG = "PlayerMainActivity"

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      setContentView(R.layout.activity_main)
      Log.d(TAG, "Activity on create here")

//      val viewModel = ViewModelProviders.of(this).get(MediaViewModel::class.java)

       val sharedPref = this?.getPreferences(Context.MODE_PRIVATE) ?: return
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
