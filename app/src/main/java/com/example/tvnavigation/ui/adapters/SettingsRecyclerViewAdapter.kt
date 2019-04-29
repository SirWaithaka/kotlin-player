package com.example.tvnavigation.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tvnavigation.R

class SettingsRecyclerViewAdapter(
      private val items: HashMap<String, String>
): RecyclerView.Adapter<SettingsRecyclerViewAdapter.ViewHolder>() {

   private val TAG = "SettingsRecyclerView"

   override fun getItemCount(): Int {
      return items.size
   }

   override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      Log.d(TAG, "on BindView called")

      val titles = items.keys
      val viewTitle: String = titles.elementAt(position)
      val viewDescription: String = items[viewTitle]!!

      holder.title.text = viewTitle
      holder.description.text = viewDescription
   }

   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
      val layoutView = LayoutInflater.from(parent.context).inflate(R.layout.settings_list_item, parent, false)
      return ViewHolder(layoutView)
   }

   inner class ViewHolder(itemView : View): RecyclerView.ViewHolder(itemView) {

      val title: TextView = itemView.findViewById(R.id.settingsList_item_title)
      val description: TextView = itemView.findViewById(R.id.settingsList_item_description)

      // we can set onClick listener on this parentView
//      val parentLayout: View = itemView.findViewById(R.id.settingsList_item) as View
   }
}