package com.youtise.player.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.youtise.player.R
import com.youtise.player.data.db.models.MediaModel

class PlaylistRecyclerViewAdapter(
      private val mediaList: List<MediaModel>
): RecyclerView.Adapter<PlaylistRecyclerViewAdapter.ViewHolder>() {

//   private val TAG = "PlaylistRecyclerViewAdapter"
   override fun getItemCount(): Int {
      return mediaList.size
   }

   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
      val layoutView = LayoutInflater.from(parent.context).inflate(R.layout.playlist_item, parent, false)
      return ViewHolder(layoutView)
   }

   override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      val mediaName: String = mediaList[position].name
      val mediaType: String = mediaList[position].type
      val timeOfPlay: List<String> = mediaList[position].timesOfDay
      val stringBuilder = StringBuilder("Play time: ")
      stringBuilder.append(timeOfPlay.joinToString(", "))

      holder.mediaNameTxView.text = mediaName
      holder.mediaTypeTxView.text = mediaType
      holder.mediaTimeOfPlayTxView.text = stringBuilder.toString()
   }


   inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
      val mediaNameTxView: TextView = itemView.findViewById(R.id.mediaNameTextView)
      val mediaTypeTxView: TextView = itemView.findViewById(R.id.mediaTypeTextView)
      val mediaTimeOfPlayTxView: TextView = itemView.findViewById(R.id.mediaTimeOfPlayTextView)
   }
}