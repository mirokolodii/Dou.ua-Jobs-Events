package com.unagit.douuajobsevents.models

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

    /**
     * @param ofType type of items to be returned.
     * @return Single with a list of items with specified type.
     * @see Item
     * @see Single
     */
    private fun getItemsSingle(ofType: ItemType): Single<List<Item>> {
        return Single
                .just(dbInstance.itemDao().getItems(ofType.value))
//                .create<List<Item>> { emitter ->
//                    val items = dbInstance.itemDao().getItems(ofType.value)
//                    emitter.onSuccess(items)
//                }
    }

    fun getEventsSingle(): Single<List<Item>> {
        return getItemsSingle(ItemType.EVENT)
    }

    fun getVacanciesSingle(): Single<List<Item>> {
        return getItemsSingle(ItemType.JOB)
    }

    private fun getPagedItemsObservable(ofType: ItemType): Observable<PagedList<Item>> {
        val dataSourceFactory = dbInstance.itemDao().getPagedItems(ofType.value)
        return RxPagedListBuilder(dataSourceFactory, DATABASE_PAGE_SIZE).buildObservable()
//        return Single.just()
//        return data
    }

    fun getPagedEventsObservable(): Observable<PagedList<Item>> {
        return getPagedItemsObservable(ItemType.EVENT)
    }

    fun getPagedVacanciesObservable(): Observable<PagedList<Item>> {
        return getPagedItemsObservable(ItemType.JOB)
    }

    fun getFavouritesSingle(): Single<List<Item>> {
        return Single.just(dbInstance.itemDao().getFavourites())
//                .create<List<Item>> { emitter ->
//                    val favs = dbInstance.itemDao().getFavourites()
//                    emitter.onSuccess(favs)
//                }
    }

    fun getItemWithIdSingle(guid: String): Single<Item> {
        return Single.just(dbInstance.itemDao().getItemWithId(guid))
//                .create { emitter ->
//            val item = dbInstance.itemDao().getItemWithId(guid)
//            emitter.onSuccess(item)
//        }
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

    fun getDeleteItemCompletable(item: Item): Completable {
        return Completable.create { emitter ->
            dbInstance.itemDao().delete(item)
            emitter.onComplete()
        }
    }
}