package com.unagit.douuajobsevents.models

import androidx.room.*

/**
 * Room DAO for an Item.
 * @see Item
 */
@Dao
interface ItemDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
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