package com.unagit.douuajobsevents.models

import android.app.Application
import android.os.AsyncTask
import android.util.Log
import androidx.core.text.HtmlCompat
import com.unagit.douuajobsevents.helpers.ItemType
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class DataProvider(var application: Application?) /* : Callback<ItemDataWrapper> */ {

    private val douApiService = DouAPIService.create()
    private val logTag = "RetrofitDataProvider"
//    private var items: List<Item>? = null


    companion object {
        private var DB_INSTANCE: AppDatabase? = null

//        class InsertAsyncTask : AsyncTask<Item, Void, Void>() {
//            override fun doInBackground(vararg params: Item?): Void? {
//                DB_INSTANCE?.itemDao()?.insert(params[0]!!)
//                return null
//            }
//        }
//
//        class GetItemsAsyncTask : AsyncTask<Void, Void, List<Item>?>() {
//
//            override fun doInBackground(vararg params: Void?): List<Item>? {
//                return DB_INSTANCE?.itemDao()?.getItems()
//            }
//
//            override fun onPostExecute(result: List<Item>?) {
////                super.onPostExecute(result)
//                result?.forEach {
//                    Log.d("DBTest", it.title)
//                }
//
//            }
//        }
    }

    init {
        DB_INSTANCE = AppDatabase.getInstance(application!!)

        // Refresh data from network
//        douApiService.getEvents().enqueue(this)
    }

    fun detach() {
        this.application = null
    }


    fun getItemsObservable(): Observable<List<Item>> {
        return Observable
                .create<List<Item>> { emitter ->
                    val localItems = DB_INSTANCE!!.itemDao().getItems()
                    emitter.onNext(localItems)
                    emitter.onComplete()
                }

    }

    fun getItemsObservable(ofType: ItemType): Single<List<Item>> {
        return Single
                .create<List<Item>> { emitter ->
                    val localItems =
                            if (ofType.value == ItemType.EVENT.value) {
                                DB_INSTANCE!!.itemDao().getItems(ItemType.EVENT.value)
                            } else {
                                DB_INSTANCE!!.itemDao().getItems(ItemType.JOB.value)
                            }
                    emitter.onSuccess(localItems)
                }
    }

    fun getFavouritesObservable(): Single<List<Item>> {
        return Single
                .create<List<Item>> { emitter ->
                    val localItems = DB_INSTANCE!!.itemDao().getFavourites()
                    emitter.onSuccess(localItems)
                }
    }

    fun getItemWithIdObservable(guid: String): Single<Item> {
        return Single.create { emitter ->
            val item = DB_INSTANCE!!.itemDao().getItemWithId(guid)
            emitter.onSuccess(item)
        }
    }

    fun getDeleteLocalDataObservable(): Completable {
        return Completable.create { emitter ->
            DB_INSTANCE!!.itemDao().deleteAll()
            emitter.onComplete()
        }
    }

    fun getRefreshDataObservable(): Observable<List<Item>> {

        val eventsObservable = douApiService.getEventsObservable()
                .map {
                    Log.d(logTag, "received Observable from retrofit call with ${it.items.size} elements.")
                    val localItems = DB_INSTANCE!!.itemDao().getItems()
                    it.items

                            // Filter out those items, which are already in local DB
                            .filter { xmlItem ->
                                !localItems.any { item ->
                                    item.guid == xmlItem.guid
                                }
                            }


                            // Convert XmlItem into Item
                            // and save Item into local DB
                            .map { xmlItem ->
                                val item = getEventFrom(xmlItem)
                                DB_INSTANCE?.itemDao()?.insert(item)
                                Log.d(logTag, "Created item with imageUrl ${item.imgUrl}.")
                                item
                            }
                }
        val vacanciesObservable = douApiService.getVacanciesObservable()
                .map {
                    val localItems = DB_INSTANCE!!.itemDao().getItems()
                    it.items
                            .filter { xmlItem ->
                                !localItems.any { item ->
                                    item.guid == xmlItem.guid
                                }
                            }
                            .map { xmlItem ->
                                val item = getJobFrom(xmlItem)
                                DB_INSTANCE?.itemDao()?.insert(item)
                                Log.d(logTag, "Created item with imageUrl ${item.imgUrl}.")
                                item
                            }
                }

        return Observable.merge<List<Item>>(eventsObservable, vacanciesObservable)
    }

    /**
     * Converts XmlItem into job as Item object.
     * Parses html code in body, extracts img url and description.
     * @param xmlItem XmlItem received from web
     * @return Item, converted from xml.
     * @see XmlItem
     * @see Item
     */
    private fun getJobFrom(xmlItem: XmlItem): Item {
        val guid = xmlItem.guid
        val title = prepareHtmlTitle(xmlItem.title)
        val timestamp = Calendar.getInstance().timeInMillis
        val type = ItemType.JOB.value
        val imgUrl = ""
        val isFavourite = false
        val description = xmlItem.description
        return Item(
                guid,
                title,
                type,
                imgUrl,
                description,
                timestamp,
                false
        )
    }

    /**
     * Converts XmlItem into event as Item object.
     * Parses html code in body, extracts img url and description.
     * @param xmlItem XmlItem received from web
     * @return Item, converted from xml.
     * @see XmlItem
     * @see Item
     */
    private fun getEventFrom(xmlItem: XmlItem): Item {
        val guid = xmlItem.guid
        val title = prepareHtmlTitle(xmlItem.title)
        val doc: Document = Jsoup.parseBodyFragment(xmlItem.description)

        // Get image url from first paragraph
        val imgUrl = doc.body().selectFirst("p").selectFirst("img").attr("src")

        // Get HTML paragraphs omitting first two
        val description = doc.select("body > :gt(1)").html()
        val timestamp = Calendar.getInstance().timeInMillis
        val type = ItemType.EVENT.value

        return Item(guid, title, type, imgUrl, description, timestamp, false)
    }

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