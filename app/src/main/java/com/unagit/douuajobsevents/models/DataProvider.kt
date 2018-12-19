package com.unagit.douuajobsevents.models

import com.unagit.douuajobsevents.MyApp
import com.unagit.douuajobsevents.helpers.ItemType
import com.unagit.douuajobsevents.helpers.Language
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

/**
 * This class is responsible for providing data to the app from tho sources:
 * 1. from local db with help of Room,
 * 2. from web, using Retrofit.
 * @param application is required to create an instance of local db.
 */
class DataProvider(private val dbInstance: AppDatabase) {

    /**
     * Instance of Retrofit API service.
     */
    private val douApiService = DouAPIService.create()

    /**
     * @return Observable with a list of locally stored items.
     * @see Observable
     */
    fun getItemsObservable(): Single<List<Item>> {
        return Single
                .create<List<Item>> { emitter ->
                    val localItems = dbInstance.itemDao().getItems()
                    emitter.onSuccess(localItems)
                }

    }

    /**
     * @param guid an ID of an Item to be returned.
     * @return Single with a single Item from local db.
     * @see Item
     * @see Single
     */
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
            val item = dbInstance.itemDao().getItemWithId(guid)
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
            dbInstance.itemDao().deleteAll()
            emitter.onComplete()
        }
    }

    fun changeItemFavourite(toBeFav: Boolean, guid: String): Completable {
        return Completable.create { emitter ->
            dbInstance.itemDao().setAsFav(toBeFav, guid)
            emitter.onComplete()
        }
    }


    /**
     * @return Observable with a list of new Items, which are not yet available in local db.
     * @see Observable
     */
    fun getRefreshDataObservable(): Observable<List<Item>> {

        val eventsObservable = douApiService.getEventsObservable()
                .map {
                    Log.d(logTag, "received Observable from retrofit call with ${it.items.size} elements.")
                    val localItems = DB_INSTANCE!!.itemDao().getItems()
                    it.items

                    it.xmlItems
                            // TODO instead of using streams, you can use Rx methods (flatMap(), filter()) to achieve same result.
                            // Filter out those items, which are already in local DB
                            .filter { xmlItem ->

                                // Get a list of locally stored items
                                val localItems = dbInstance.itemDao().getItems()

                                // Return true, only if xmlItem IS NOT present in localItems
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

                            // Convert XmlItem object into Item object and save item into local DB,
                            // return this item
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
        val imgUrl = getImgUrlFromTitle(title)
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

    private fun getImgUrlFromTitle(title: String): String {
        val languages = Language.values()
        languages.forEach {
            if(title.contains(it.name,true)) {
                return it.url
            }
        }

        return Language.DEFAULT.url

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