package com.example.henryjacobs.shoppinglist.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context


@Database(entities = arrayOf(Item::class), version = 1) // add all entities arrayOf, so if there was a table of Person
// it would be Person::class
abstract class AppDatabase : RoomDatabase() {

    abstract fun itemDao(): ItemDAO

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.applicationContext,
                        AppDatabase::class.java, "items.db")
                        .build()
            }
            return INSTANCE!!
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}