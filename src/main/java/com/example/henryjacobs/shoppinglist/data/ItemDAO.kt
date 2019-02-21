package com.example.henryjacobs.shoppinglist.data

import android.arch.persistence.room.*

@Dao
interface ItemDAO {

    @Query("SELECT * FROM items")
    fun findAllItems(): List<Item>

    @Insert
    fun insertItem(item: Item) : Long // adding an object to the table returns the id of that obj

    @Delete
    fun deleteItem(item: Item)

    @Update
    fun updateItem(item: Item)

    @Query("DELETE FROM items")
    fun deleteAll()
}