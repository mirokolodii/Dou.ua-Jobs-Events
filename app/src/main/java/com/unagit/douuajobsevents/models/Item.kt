package com.unagit.douuajobsevents.models

import android.text.Spanned
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.unagit.douuajobsevents.helpers.ItemType

@Entity(tableName = "entity_table")
data class Item(
        @PrimaryKey
        @NonNull
        val guid: String,
        val title: String,
        val type: ItemType,
        val imgUrl: String,
        val description: Spanned
)