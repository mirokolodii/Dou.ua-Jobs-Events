package com.unagit.douuajobsevents.helpers

import android.util.Log

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
    const val UNIQUE_REFRESH_WORKER_NAME = "com.unagit.douuajobsevents.services.refreshworker_unique_name"
}

object Messages {
    const val LOCAL_ITEMS_DELETE_SUCCESS_MESSAGE = "Local data has been deleted"
    const val LOCAL_ITEMS_DELETE_ERROR_MESSAGE = "Something went wrong while trying to delete local cache"
    const val LOCAL_ITEMS_GET_ERROR_MESSAGE = "Error: can't receive data"
    const val REFRESH_ERROR_MESSAGE = "Error - can't refresh data"
    const val REFRESH_NO_NETWORK_MESSAGE = "Can't refresh: no network access"
    const val DELETE_ERROR_MESSAGE = "Internal error while trying to delete item"

    fun getMessageForCount(count: Int): String {
        return when (count) {
            0 -> "No new items"
            1 -> "$count new item received"
            else -> "$count new items received"
        }
    }

    fun printLogError(e: Throwable) {
        val stackTrace = e.stackTrace[1]
        val fileName = stackTrace.fileName
        Log.e(fileName,"${e.message}. ${stackTrace.methodName}: ${stackTrace.lineNumber}")
    }

}

/**
 *  Represents two types of Item:
 *  1. Event with value = 1
 *  2. Job (Vacancy) with value = 2
 *  @param value integer that represents value of ItemType.
 */
enum class ItemType(val value: Int) {
    EVENT(1),
    JOB(2),
    FAV(3)
}

enum class Tab {
    EVENTS,
    VACANCIES,
    FAVOURITES
}
