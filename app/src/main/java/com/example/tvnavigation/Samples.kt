package com.example.tvnavigation

import android.net.Uri

data class Samples(
    var VIDEO_URI: Uri = Uri.parse("asset:///thor_ragnarok.mp4"),
    var IMAGE_URI: Uri = Uri.parse("asset:///angry_adult.jpg")
)