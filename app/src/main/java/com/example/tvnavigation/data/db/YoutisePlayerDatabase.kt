package com.example.tvnavigation.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tvnavigation.data.db.entities.Location


val DATABASE_NAME = "youtise_player_v2.db"

@Database(
      entities = [Location::class],
      version = 1 // version for the db
)
abstract class YoutisePlayerDatabase: RoomDatabase() {
   abstract fun locationDao(): LocationDao

   companion object {
      @Volatile private var instance: YoutisePlayerDatabase? = null
      private var LOCK = Any()

      operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
         instance ?: buildDatabase(context).also { instance = it }
      }

      private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, YoutisePlayerDatabase::class.java, DATABASE_NAME)
                  .build()
   }
}