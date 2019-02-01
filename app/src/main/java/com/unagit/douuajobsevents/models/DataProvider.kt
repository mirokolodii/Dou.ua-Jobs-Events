package com.unagit.douuajobsevents.models

import androidx.paging.DataSource
import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import com.unagit.douuajobsevents.helpers.ItemType
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

/**
 * This class is responsible for providing data to the app from tho sources:
 * 1. from local db with help of Room,
 * 2. from web, using Retrofit.
 * @param dbInstance instance of local db.
 */
class DataProvider(private val dbInstance: AppDatabase) {

    /**
     * Instance of Retrofit API service.
     */
    private val douApiService = DouAPIService.create()

    companion object {
        private const val DATABASE_PAGE_SIZE = 30
    }

    fun getEventsObservable(): Observable<PagedList<Item>> {
        val factory = dbInstance.itemDao().getPagedItems(ItemType.EVENT.value)
        return getObservableWith(factory)
    }

    fun getVacanciesObservable(): Observable<PagedList<Item>> {
        val factory = dbInstance.itemDao().getPagedItems(ItemType.JOB.value)
        return getObservableWith(factory)
    }

    fun getFavouritesObservable(): Observable<PagedList<Item>> {
        val factory = dbInstance.itemDao().getPagedFavItems()
        return getObservableWith(factory)
    }

    fun getSearchEventsObservable(value: String): Observable<PagedList<Item>> {
        val factory = dbInstance.itemDao().getPagedSearchItems(value, ItemType.EVENT.value)
        return getObservableWith(factory)
    }

    fun getSearchVacanciesObservable(value: String): Observable<PagedList<Item>> {
        val factory = dbInstance.itemDao().getPagedSearchItems(value, ItemType.JOB.value)
        return getObservableWith(factory)
    }

    fun getSearchFavObservable(value: String): Observable<PagedList<Item>> {
        val factory = dbInstance.itemDao().getPagedSearchFavItems(value)
        return getObservableWith(factory)
    }

    /**
     * @param ofType type of items to be returned.
     * @return Single with a list of items with specified type.
     * @see Item
     * @see Single
     */
    private fun getItemsObservable(ofType: ItemType): Observable<PagedList<Item>> {
        val dataSourceFactory = dbInstance.itemDao().getPagedItems(ofType.value)
        return RxPagedListBuilder(dataSourceFactory, DATABASE_PAGE_SIZE).buildObservable()
    }

    private fun getObservableWith(factory: DataSource.Factory<Int, Item>): Observable<PagedList<Item>>{
        return RxPagedListBuilder(factory, DATABASE_PAGE_SIZE).buildObservable()
    }

    fun getItemWithIdSingle(guid: String): Single<Item> {
        return dbInstance.itemDao().getItemWithId(guid)
    }

    /**
     * Deletes all items from local db.
     * @return Completable, once completed.
     * @see Completable
     */
    fun getDeleteLocalDataCompletable(): Completable {
        return Completable.create { emitter ->
            dbInstance.itemDao().deleteAll()
            emitter.onComplete()
        }
    }

    fun switchFavouriteState(toBeFav: Boolean, guid: String): Completable {
        return Completable.create { emitter ->
            dbInstance.itemDao().setAsFav(toBeFav, guid)
            emitter.onComplete()
        }
    }

    fun getDeleteItemCompletable(item: Item): Completable {
        return Completable.create { emitter ->
            dbInstance.itemDao().delete(item)
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
                    val localItems = dbInstance.itemDao().getItems()
                    it.xmlItems
                            // TODO instead of using streams, you can use Rx methods (flatMap(), filter()) to achieve same result.
                            // Filter out those items, which are already in local DB
                            .filter { xmlItem ->

                                // Get a list of locally stored items
//                                val localItems = dbInstance.itemDao().getItems()

                                // Return true, only if xmlItem IS NOT present in localItems
                                !localItems.any { item ->
                                    item.guid == xmlItem.guid
                                }
                            }

                            // Convert XmlItem into Item
                            // and save Item into local DB
                            .map { xmlItem ->
                                val item = xmlItem.transformEventToItem()
                                dbInstance.itemDao().insert(item)
                                item
                            }
                }
        val vacanciesObservable = douApiService.getVacanciesObservable()
                .map {
                    val localItems = dbInstance.itemDao().getItems()
                    it.xmlItems
                            .filter { xmlItem ->
                                !localItems.any { item ->
                                    item.guid == xmlItem.guid
                                }
                            }

                            // Convert XmlItem object into Item object and save item into local DB,
                            // return this item
                            .map { xmlItem ->
                                val item = xmlItem.transformJobToItem()
                                dbInstance.itemDao().insert(item)
                                item
                            }
                }

        return Observable.merge<List<Item>>(eventsObservable, vacanciesObservable)
    }
}