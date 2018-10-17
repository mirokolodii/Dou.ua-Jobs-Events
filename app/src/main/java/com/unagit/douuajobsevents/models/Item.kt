package com.unagit.douuajobsevents.models

import com.unagit.douuajobsevents.helpers.ItemType
import java.util.*

data class Item(val title: String,
                val guid: String,
                val type: ItemType,
                val imgUrl: String,
                val description: String)