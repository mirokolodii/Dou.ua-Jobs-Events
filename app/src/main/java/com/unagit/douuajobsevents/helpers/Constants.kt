package com.unagit.douuajobsevents.helpers

import android.util.Log
import java.lang.Exception

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
    const val DELETE_COMPLETED_MESSAGE = "Item deleted"
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
    JOB(2)
}

enum class Tab {
    EVENTS,
    VACANCIES,
    FAVOURITES
}

enum class Language(val url: String) {
    JAVA("https://media-market.edmodo.com/media/public/e0dd66f726194a1feb7350ae91e011d7ec811599.png"),
    PHP("https://pbs.twimg.com/profile_images/815698345716912128/hwUcGZ41.jpg"),
    ANDROID("https://www.android.com/static/2016/img/logo-android-green_1x.png"),
    IOS("https://i-cdn.phonearena.com/images/article/98836-image/Apple-releases-iOS-11.0.3-its-fourth-update-in-the-last-four-weeks.jpg"),
    NET("https://upload.wikimedia.org/wikipedia/commons/thumb/6/6d/.net_logo.svg/275px-.net_logo.svg.png"),
    PYTHON("https://cdn-images-1.medium.com/max/1200/1*PPIp7twJJUknfohZqtL8pQ.png"),
    RUBY("https://cdn-images-1.medium.com/max/1200/1*sZSVVtdP9TE3mUoGh4GoYA.png"),
    MANAGER("https://cdn0.iconfinder.com/data/icons/hr-recruitment-management/400/HR-28-512.png"),
    DEVOPS("https://cdn-images-1.medium.com/max/1200/1*CSZxfOMlVsKsrMkqTxFiMQ.png"),
    ANALYST("https://www.modernanalyst.com/Portals/0/Users/009/09/9/Business-Analyst-Role.jpg"),
    NODE("https://upload.wikimedia.org/wikipedia/commons/thumb/d/d9/Node.js_logo.svg/330px-Node.js_logo.svg.png"),
    HR("https://cdn2.iconfinder.com/data/icons/seo-web-optomization-ultimate-set/512/market_research-512.png"),
    DEFAULT("http://www.englishspectrum.com/wp-content/uploads/2016/08/7220_photo_1451393691_temp.jpg")
}