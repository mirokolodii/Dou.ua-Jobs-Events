package com.unagit.douuajobsevents.models

import android.app.Application
import com.unagit.douuajobsevents.helpers.ItemType
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.util.*

/**
 * This class is responsible for providing data to the app from tho sources:
 * 1. from local db with help of Room,
 * 2. from web, using Retrofit.
 * @param application is required to create an instance of local db.
 */
class DataProvider(var application: Application?) /* : Callback<ItemDataWrapper> */ {

    /**
     * Instance of Retrofit API service.
     */
    private val douApiService = DouAPIService.create()

    // Constants
    companion object {
        /**
         * Instance of local Room db.
         */
        private var DB_INSTANCE: AppDatabase? = null

    }

    init {
        // Initialize local db instance
        DB_INSTANCE = AppDatabase.getInstance(application!!)
    }

    fun detach() {
        this.application = null
    }

    /**
     * @return Observable with a list of locally stored items.
     * @see Observable
     */
    // TODO If you make just one onNext() call, you should use Single instead of Observable.
    // TODO Observable should be used, if you will receive items one by one in some unpredictable amount of time.
    fun getItemsObservable(): Observable<List<Item>> {
        return Observable
                .create<List<Item>> { emitter ->
                    val localItems = DB_INSTANCE!!.itemDao().getItems()
                    emitter.onNext(localItems)
                    emitter.onComplete()
                }

    }

    /**
     * @param guid an ID of an Item to be returned.
     * @return Single with a single Item from local db.
     * @see Item
     * @see Single
     */
    fun getItemWithIdObservable(guid: String): Single<Item> {
        return Single.create { emitter ->
            val item = DB_INSTANCE!!.itemDao().getItemWithId(guid)
            emitter.onSuccess(item)
        }
    }

    /**
     * Deletes all items from local db.
     * @return Completable, once completed.
     * @see Completable
     */
    fun getDeleteLocalDataObservable(): Completable {
        return Completable.create { emitter ->
            DB_INSTANCE!!.itemDao().deleteAll()
            emitter.onComplete()
        }
    }

    fun changeItemFavourite(toBeFav: Boolean, guid: String): Completable {
        return Completable.create { emitter ->
            DB_INSTANCE!!.itemDao().setAsFav(toBeFav, guid)
            emitter.onComplete()
        }
    }


    /**
     * @return Observable with a list of new Items, which are not yet available in local db.
     * @see Observable
     */
    fun getRefreshDataObservable(): Observable<List<Item>> {
        return douApiService.getEventsObservable()

                // Extract ItemDataWrapper into a list of xml items.
                .map {
                    it.xmlItems
                            // TODO instead of using streams, you can use Rx methods (flatMap(), filter()) to achieve same result.
                            // Filter out those items, which are already in local DB
                            .filter { xmlItem ->

                                // Get a list of locally stored items
                                val localItems = DB_INSTANCE!!.itemDao().getItems()

                                // Return true, only if xmlItem IS NOT present in localItems
                                !localItems.any { item ->
                                    item.guid == xmlItem.guid
                                }
                            }

                            // Convert XmlItem object into Item object and save item into local DB,
                            // return this item
                            .map { xmlItem ->
                                val item = getItemFrom(xmlItem)
                                DB_INSTANCE?.itemDao()?.insert(item)
                                item
                            }
                }

    }

    private fun getItemFrom(xmlItem: XmlItem): Item {
        val guid = xmlItem.guid
        val title = prepareHtmlTitle(xmlItem.title)
        val doc: Document = Jsoup.parseBodyFragment(xmlItem.description)

        // Get image url from first paragraph
        val imgUrl = doc.body().selectFirst("p").selectFirst("img").attr("src")

        // Get HTML paragraphs omitting first two
        val description = doc.select("body > :gt(1)").html()

        val type = ItemType.EVENT.value

        return Item(guid, title, type, imgUrl, description)
    }

    /**
     * Adds HTML bold tabs (<b></b>) to a part of title before first occurance of ',' symbol,
     * followed by a 'new line' (<br>) tag.
     * Example:
     * @code {val input = "A Java conference, 17th of December, Lviv"}
     * @code {prepareHtmlTitle(input)} // returns "<b>A Java conference</b><br>, 17th of December, Lviv"
     * @param title to be changed.
     * @return title with added HTML tags.
     */
    private fun prepareHtmlTitle(title: String): String {
        val commaIndex = title.indexOf(",")

        return StringBuilder()
                .append("<b>")
                .append(title.substring(0, commaIndex))
                .append("</b>")
                .append(",<br>")
                .append(title.substring(commaIndex + 1).trim())
                .toString()
    }
}