package com.example.youtiseprototype

import android.os.Bundle
import android.app.Activity
import android.database.Cursor
import android.net.Uri
import android.support.v7.widget.RecyclerView

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {

        lateinit var recylerView :RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    fun init() {
        recylerView = findViewById(R.id.recyclerViewVideo)
fetchVideoFromGallery()
    }

    private fun fetchVideoFromGallery() {
        var uri: Uri
        var cursor: Cursor

    }


}
