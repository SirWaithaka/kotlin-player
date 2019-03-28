package com.example.tvnavigation.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.tvnavigation.data.db.entities.Location

class SpinnerAdapter(
      context: Context,
      private val resource: Int,
      private var locationsList: List<Location>
   ): ArrayAdapter<Location>(context, 0, locationsList) {

   private val inflater: LayoutInflater = LayoutInflater.from(context)

   override fun getCount(): Int {
      return locationsList.size
   }

   override fun getItem(position: Int): Location {
      return locationsList[position]
   }

   /**
    * Responsible for inflating and providing the unique views in the spinner
    * @param convertView - passed into the fn as a reusable view
    */
   override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
      return initView(position, convertView, parent)
   }

   override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
      return initView(position, convertView, parent)
   }

   private fun initView(position: Int, convertView: View?, parent: ViewGroup?): View {
      val view: View
      val viewHolder: LocationViewHolder

      // check if the list gives us a view to reuse
      // if not create a new view
      if (convertView == null) {
         view = inflater.inflate(resource, parent, false)
         viewHolder = LocationViewHolder(view)
      } else {
         view = convertView
         viewHolder = view.tag as LocationViewHolder
      }

      val currentItem = locationsList[position]
      viewHolder.locationName.text = currentItem.placeName

      return view
   }
 }