package com.unagit.douuajobsevents.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.unagit.douuajobsevents.helpers.RoomConstants.DB_NAME
import com.unagit.douuajobsevents.model.Item
import com.unagit.douuajobsevents.model.ItemDao

/**
 * Local Room database.
 * Entities is a list of all model classes, annotated with Room's @Entity.
 */
@Database(entities = [Item::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    // List of all Room DAOs
    abstract fun itemDao(): ItemDao

    // Constants
    companion object {
        private var INSTANCE: AppDatabase? = null

        /**
         * @return an instance of app's Room database.
         * @see Room
         */
        fun getInstance(context: Context): AppDatabase {
            if(INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, DB_NAME)
                            .build()
                }
            }
            return INSTANCE!!
        }

    }
}