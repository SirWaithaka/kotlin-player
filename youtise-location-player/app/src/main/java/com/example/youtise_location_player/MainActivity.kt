package com.example.youtise_location_player

import android.os.Bundle
import android.app.Activity
import android.content.Intent
import android.view.View


class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    fun startSetup(view: View) {

        val intent = Intent(this, Settings::class.java)
        startActivity(intent)

    }
}
