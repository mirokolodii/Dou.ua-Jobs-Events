package com.unagit.douuajobsevents.helpers

/**
 * Helpers with some constants and enums.
 */

// Base URLs that are used by Retrofit to receive data from web
object RetrofitConstants {
    const val DOU_UA_BASE_API_URL = "https://dou.ua/"
    const val DOU_UA_CALENDAR_API_URL = "https://dou.ua/calendar/feed/"
    const val DOU_UA_VACANCIES_API_URL = "https://jobs.dou.ua/vacancies/feeds/"
}

object WorkerConstants {
    const val UNIQUE_REFRESH_WORKER_NAME
            = "com.unagit.douuajobsevents.services.refreshworker_unique_name"
}

// Represents two types of Item - Event and Job (Vacancy)
enum class ItemType(val value: Int) {
    EVENT(1),
    JOB(2)
}