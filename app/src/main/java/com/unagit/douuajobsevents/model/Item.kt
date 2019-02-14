package com.unagit.douuajobsevents.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/**
 * Data model for an item.
 * '@Entity' annotation sets a Room Entity
 * with 'tableName' parameter specifying db table name.
 * 'type' is an integer value of ItemType object (either Event or Job).
 * timestamp is a time in milliseconds of a moment, when item is added to local db.
 */
@Entity(tableName = "entity_table")
data class Item(
        @PrimaryKey @NonNull var guid: String,
        @NonNull val title: String,
        @NonNull val type: Int,
        @NonNull @ColumnInfo(name = "img_url") var imgUrl: String,
        @NonNull val description: String,
        @NonNull val timestamp: Long = Calendar.getInstance().timeInMillis,
        @NonNull var isFavourite: Boolean = false
)