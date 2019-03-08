package com.example.youtise_location_player

import android.os.Bundle
import android.app.Activity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner

import kotlinx.android.synthetic.main.activity_locations.*

class Locations : Activity(), AdapterView.OnItemSelectedListener {

    lateinit var locations: Spinner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_locations)
        init()
    }

    private fun init() {
        val locationsArray = listOf("DevCon", "Two rivers", "The Junction")

        locations = findViewById(R.id.locations)
        val dataAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, locationsArray)

        locations.setAdapter(dataAdapter)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}
