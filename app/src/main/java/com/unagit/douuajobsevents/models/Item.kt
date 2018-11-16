package com.unagit.douuajobsevents.models

import android.text.Spanned
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.unagit.douuajobsevents.helpers.ItemType

@Entity(tableName = "entity_table")
data class Item(
        @PrimaryKey @NonNull var guid: String,
        val title: String,
        val type: Int,
        @ColumnInfo(name = "img_url") var imgUrl: String,
        val description: String,
        val timestamp: Long

)