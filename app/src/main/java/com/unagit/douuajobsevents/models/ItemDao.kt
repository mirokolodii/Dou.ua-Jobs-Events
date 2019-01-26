package com.unagit.douuajobsevents.models

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Delete
import androidx.room.Query
import androidx.room.OnConflictStrategy

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

    @Query("SELECT * from entity_table WHERE type = :type ORDER BY timestamp DESC")
    fun getItems(type: Int): List<Item>

    @Query("SELECT * from entity_table WHERE isFavourite = 1 ORDER BY timestamp DESC")
    fun getFavourites(): List<Item>

    @Query("SELECT * from entity_table WHERE guid = :guid LIMIT 1")
    fun getItemWithId(guid: String): Item

    @Query("DELETE from entity_table")
    fun deleteAll()

    @Query("UPDATE entity_table SET isFavourite = :toBeFav WHERE guid = :guid")
    fun setAsFav(toBeFav: Boolean, guid: String)

    @Query("SELECT * from entity_table WHERE type = :type ORDER BY timestamp DESC")
    fun getPagedItems(type: Int): DataSource.Factory<Int, Item>
}