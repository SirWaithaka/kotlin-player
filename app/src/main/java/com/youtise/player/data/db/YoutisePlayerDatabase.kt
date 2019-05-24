package com.example.player.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.player.data.db.entities.*
import com.example.player.data.db.views.DeviceView


const val DATABASE_NAME = "youtise_player_v10.18.db"

@Database(
      entities = [Location::class,Device::class,Advert::class],
      version = 1, // version for the db
      exportSchema = false,
      views = [DeviceView::class]
)
@TypeConverters(ListToString::class,DateTimeToString::class)
abstract class YoutisePlayerDatabase: RoomDatabase() {
   /*
      In the background Room will create an instance of the LocationDao
      with all implementation. When this abstract fun is called it will
      return an instance of the Dao
      @return LocationDao
    */
   abstract fun locationDao(): LocationDao
   abstract fun advertDao(): AdvertDao
   abstract fun deviceDao(): DeviceDao

   companion object {
      @Volatile private var instance: YoutisePlayerDatabase? = null
      private var LOCK = Any()

      // creates a singleton for the database, across multiple threads
      operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
         instance ?: buildDatabase(context).also { instance = it }
      }

      /**
       * Build persistent app database and return it
       * @param context
       */
      private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, YoutisePlayerDatabase::class.java, DATABASE_NAME)
                  .build()
   }
}