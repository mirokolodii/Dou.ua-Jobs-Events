package com.unagit.douuajobsevents.models

import androidx.room.*

@Dao
interface ItemDao{

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(item: Item)

    @Delete
    fun delete(item: Item)

    @Query("SELECT * from entity_table ORDER BY timestamp DESC")
    fun getItems(): List<Item>

    @Query("SELECT * from entity_table WHERE guid = :guid LIMIT 1")
    fun getItemWithId(guid: String): Item

    @Query("DELETE from entity_table")
    fun deleteAll()
}