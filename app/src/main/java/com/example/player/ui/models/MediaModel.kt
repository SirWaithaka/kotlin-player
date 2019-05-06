package com.example.player.ui.models

abstract class MediaModel {
   abstract val name: String
   abstract val type: String
   abstract val timesOfDay: List<String>
   open val mediaLocalPath: String = ""
}