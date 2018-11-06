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
        var guid: String,

        var title: String,
        var type: ItemType,
        var imgUrl: String,
        var description: Spanned
)