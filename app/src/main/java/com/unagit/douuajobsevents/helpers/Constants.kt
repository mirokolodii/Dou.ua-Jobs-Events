package com.unagit.douuajobsevents.helpers

object TabsObj {
    const val TABS_COUNT = 2
    const val TAB_BUNDLE_LABEL = "tabLabel"
    enum class Tabs(val pos: Int, val title: String) {
        TAB_1(0, "Events"),
        TAB_2(1, "Jobs")
    }
}

object RetrofitConstants {
    const val DOU_UA_BASE_API_URL = "https://dou.ua/"
    const val DOU_UA_CALENDAR_API_URL = "https://dou.ua/calendar/feed/"
    const val DOU_UA_VACANCIES_API_URL = "https://jobs.dou.ua/vacancies/feeds/"
}

enum class ItemType {
    EVENT,
    JOB
}