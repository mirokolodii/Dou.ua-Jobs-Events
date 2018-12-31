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
    const val UNIQUE_REFRESH_WORKER_NAME = "com.unagit.douuajobsevents.services.refreshworker_unique_name"
}

object Messages {
    const val LOCAL_ITEMS_ERROR_MESSAGE = "Error: can't receive data"
    const val REFRESH_ERROR_MESSAGE = "Error - can't refresh data"
    const val DELETE_COMPLETED_MESSAGE = "Item deleted"
    const val DELETE_ERROR_MESSAGE = "Internal error while trying to delete item"
    fun getMessageForCount(count: Int): String {
        return when (count) {
            0 -> "No new items"
            1 -> "$count new item received"
            else -> "$count new items received"
        }
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
    ANDROID("http://mobilab.dk/wp-content/uploads/2018/07/Android-Logo-740x416.jpg"),
    IOS("https://i-cdn.phonearena.com/images/article/98836-image/Apple-releases-iOS-11.0.3-its-fourth-update-in-the-last-four-weeks.jpg"),
    NET("data:image/jpeg;base64,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"),
    PYTHON("https://cdn-images-1.medium.com/max/1200/1*PPIp7twJJUknfohZqtL8pQ.png"),
    RUBY("https://cdn-images-1.medium.com/max/1200/1*sZSVVtdP9TE3mUoGh4GoYA.png"),
    MANAGER("http://itcareercentral.com/wp-content/uploads/2015/09/roles-of-IT-project-manager.jpg"),
    DEVOPS("https://cdn-images-1.medium.com/max/1200/1*CSZxfOMlVsKsrMkqTxFiMQ.png"),
    ANALYST("https://www.modernanalyst.com/Portals/0/Users/009/09/9/Business-Analyst-Role.jpg"),
    NODE("data:image/png;base64,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"),
    HR("1data:image/jpeg;base64,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"),
    DEFAULT("http://www.englishspectrum.com/wp-content/uploads/2016/08/7220_photo_1451393691_temp.jpg")
}