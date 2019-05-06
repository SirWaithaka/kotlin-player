package com.example.player.data.db.models

import com.example.player.internal.TIME_OF_DAY


class MediaModel(
   val id: String,
   val name: String,
   val type: String,
   val timesOfDay: List<String>,
   val localMediaPath: String
) {

   val isPlayable: Boolean
      get() {
         return (TIME_OF_DAY in timesOfDay)
      }
}