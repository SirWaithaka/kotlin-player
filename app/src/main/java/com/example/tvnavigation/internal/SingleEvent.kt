package com.example.tvnavigation.internal

open class SingleEvent<out T>(private val content: T) {

   private var hasBeenHandled = false
//      private set // Allow external read but not write

   fun getContent(): T? {
      return if (hasBeenHandled) null
      else {
         hasBeenHandled = true
         content
      }
   }
}