package com.example.henryjacobs.shoppinglist.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable

// ID column in table is mandatory with SQLite and Android
@Entity(tableName = "items") // table name
data class Item(
        @PrimaryKey(autoGenerate = true) var itemId: Long?, // table must have PrimaryKey in SQLite
        @ColumnInfo(name = "category") var category: Int,
        @ColumnInfo(name = "name") var name: String,
        @ColumnInfo(name = "description") var description: String,
        @ColumnInfo(name = "price") var price: String,
        @ColumnInfo(name = "purchased") var purchased: Boolean


) : Serializable
// must be serializable so that it can be used with Intents later