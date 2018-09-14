package com.unagit.douuajobsevents.helpers

object TabsObj {
    const val TABS_COUNT = 2
    enum class Tabs(val pos: Int, val title: String) {
        TAB_1(0, "Events"),
        TAB_2(1, "Jobs")
    }
}

enum class ItemType {
    EVENT,
    JOB
}