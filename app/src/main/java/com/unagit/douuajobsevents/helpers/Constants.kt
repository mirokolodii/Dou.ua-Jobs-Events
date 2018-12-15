package com.unagit.douuajobsevents.helpers

/**
 * Helpers with some constants and enums.
 * @author Myroslav Kolodii
 * @ver %I%
 */

/**
 *  Base URLs that are used by Retrofit to receive data from web.
 */
object RetrofitConstants {
    const val DOU_UA_BASE_API_URL = "https://dou.ua/"
    const val DOU_UA_CALENDAR_API_URL = "https://dou.ua/calendar/feed/"
    const val DOU_UA_VACANCIES_API_URL = "https://jobs.dou.ua/vacancies/feeds/"
}

object RoomConstants {
    const val DB_NAME = "room db"
}

object WorkerConstants {
    const val UNIQUE_REFRESH_WORKER_NAME
            = "com.unagit.douuajobsevents.services.refreshworker_unique_name"
}

/**
 *  Represents two types of Item:
 *  1. Event with value = 1
 *  2. Job (Vacancy) with value = 2
 *  @param value integer that represents value of ItemType.
 */
enum class ItemType(val value: Int) {
    EVENT(1),
    JOB(2)
}