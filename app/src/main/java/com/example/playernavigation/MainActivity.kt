package com.example.playernavigation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {

   private val TAG = "PlayerMainActivity"

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      setContentView(R.layout.activity_main)
      Log.d(TAG, "Activity on create here")
   }

   override fun onResume() {
      super.onResume()
      Log.d(TAG, "Resume state called")
   }
}
