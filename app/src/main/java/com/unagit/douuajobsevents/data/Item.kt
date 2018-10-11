package com.unagit.douuajobsevents.data

import com.unagit.douuajobsevents.helpers.ItemType
import java.util.*

data class Item(val link: String,
                val title: String,
                val guid: String,
                val pubDate: Date,
                val type: ItemType,
                val imgUrl: String)