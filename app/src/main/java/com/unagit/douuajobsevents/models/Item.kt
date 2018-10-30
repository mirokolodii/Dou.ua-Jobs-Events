package com.unagit.douuajobsevents.models

import android.text.Spanned
import com.unagit.douuajobsevents.helpers.ItemType

data class Item(val title: String,
                val guid: String,
                val type: ItemType,
                val imgUrl: String,
                val description: Spanned)