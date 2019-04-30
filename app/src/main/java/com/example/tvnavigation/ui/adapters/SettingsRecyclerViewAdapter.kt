package com.example.tvnavigation.ui.adapters

import android.util.Log
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tvnavigation.R

class SettingsRecyclerViewAdapter : RecyclerView.Adapter<SettingsRecyclerViewAdapter.ViewHolder>() {

   private val TAG = "SettingsRecyclerView"
   private var items: HashMap<String, String> = HashMap()

   override fun getItemCount(): Int {
      return items.size
   }

   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
      val layoutView = LayoutInflater.from(parent.context).inflate(R.layout.settings_list_item, parent, false)
      return ViewHolder(layoutView)
   }

   override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      Log.d(TAG, "on BindView called")

      val titles = items.keys
      val viewTitle: String = titles.elementAt(position)
      val viewDescription: String = items[viewTitle]!!

      holder.title.text = viewTitle
      holder.description.text = viewDescription

      holder.parentLayout.setOnClickListener {

         Log.d(TAG, "$position")
         if (holder.selectedItem.get(position, false)) {
            holder.selectedItem.delete(position)
            it.isSelected = false
         } else {
            holder.selectedItem.put(position, true)
            it?.isSelected = true
         }
      }
   }

   fun setInformationData(data: HashMap<String, String>) {
      items = data
   }

   inner class ViewHolder(itemView : View): RecyclerView.ViewHolder(itemView) {

      val title: TextView = itemView.findViewById(R.id.settingsList_item_title)
      val description: TextView = itemView.findViewById(R.id.settingsList_item_description)
      // we can set onClick listener on this parentView
      val parentLayout: View = itemView.findViewById(R.id.settingsList_item) as View
      val selectedItem = SparseBooleanArray()

   }
}