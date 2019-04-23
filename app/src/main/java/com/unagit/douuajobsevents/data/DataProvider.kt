package com.unagit.douuajobsevents.data

import android.util.Log
import androidx.paging.DataSource
import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.gson.GsonBuilder
import com.unagit.douuajobsevents.BuildConfig
import com.unagit.douuajobsevents.helpers.ItemType
import com.unagit.douuajobsevents.model.Image
import com.unagit.douuajobsevents.model.Item
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single


class DataProvider(private val dbInstance: AppDatabase) {

    private val douApiService = DouAPIService.create()
    private val remoteConfig = FirebaseRemoteConfig.getInstance()
    lateinit var images: List<Image>

    companion object {
        private const val DATABASE_PAGE_SIZE = 30
    }

    init {
        val configSettings = FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .setMinimumFetchIntervalInSeconds(4200)
                .build()
        remoteConfig.setConfigSettings(configSettings)

        remoteConfig.fetchAndActivate()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val updated = task.result
                        Log.d("firebase", "Config params updated: $updated")
                        val obj = remoteConfig.getString("images")
                        Log.e("firebase", "obj is: $obj")
                        val gson = GsonBuilder().create()
//                        val imageListType = object : TypeToken<List<Image>>() {}.type
//                        images = gson.fromJson(obj, imageListType)
                        val images = gson.fromJson(obj, Array<Image>::class.java).toList()
                        images.forEach {
                            println(it.keyword)
                        }
                    } else {
                        Log.d("firebase", "fetched failed")
                    }
                }


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

    private fun getObservableWith(factory: DataSource.Factory<Int, Item>): Observable<PagedList<Item>> {
        return RxPagedListBuilder(factory, DATABASE_PAGE_SIZE).buildObservable()
    }

    fun getItemWithIdSingle(guid: String): Single<Item> {
        return dbInstance.itemDao().getItemWithId(guid)
    }

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