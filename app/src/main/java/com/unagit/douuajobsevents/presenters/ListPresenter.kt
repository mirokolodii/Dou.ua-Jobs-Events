package com.unagit.douuajobsevents.presenters

import android.app.Application
import android.os.Handler
import android.util.Log
import com.unagit.douuajobsevents.contracts.ListContract
import com.unagit.douuajobsevents.models.Item
import com.unagit.douuajobsevents.models.DataProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class ListPresenter : ListContract.ListPresenter {
    private var view: ListContract.ListView? = null
    private var dataProvider: DataProvider? = null
    private var localDataDisposable: Disposable? = null

    private val logTag = "ListPresenter"


    // Fields used for data refresh from web with interval 'refreshInterval' msec.
    private var refreshDataDisposable: Disposable? = null
    private val initialRefreshInterval = 5 * 1000L /* 5 sec */
    private val refreshInterval = 60 * 5 * 1000L /* 5 min */
    private val refreshHandler = Handler()
    private var refreshRunnable: Runnable? = null

    override fun attach(view: ListContract.ListView, application: Application) {
        this.view = view
        this.dataProvider = DataProvider(application)
        initiateDataRefresh()
    }


    override fun detach() {
        this.view = null
        localDataDisposable?.dispose()
        refreshDataDisposable?.dispose()

        // TODO: Should I detach in DataProvider?
        // -- yes: lost singleton
        // -- no: memory leakage?
//        dataProvider?.detach()

        stopDataRefresh()
    }

    private fun initiateDataRefresh() {
        refreshRunnable = Runnable {
            if(view != null && view!!.hasNetwork()) {
                refreshData()
            } else {
                view?.showSnackbar("Can't refresh: no network access.")
            }

            refreshHandler.postDelayed(refreshRunnable, refreshInterval)
        }
        refreshHandler.postDelayed(refreshRunnable, initialRefreshInterval)
    }

    private fun stopDataRefresh() {
        if(refreshRunnable != null) {
            refreshHandler.removeCallbacksAndMessages(null)
        }
    }


    override fun getItems() {
        view?.showLoading(true)

        val itemsObservable = dataProvider!!.getItemsObservable()

        localDataDisposable = itemsObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableObserver<List<Item>>() {
                    override fun onComplete() {
                    }

                    override fun onNext(t: List<Item>) {

                        view?.showLoading(false)
                        view?.showItems(t)
                    }

                    override fun onError(e: Throwable) {
                        Log.e(logTag, e.message)
                        view?.showSnackbar("Error: can't receive data from local cache.")
                    }

                })


    }

    override fun itemClicked(position: Int) {
        val guid = dataProvider?.getGuidForItemIn(position)

        if(guid != null) {
        view?.showItemDetails(position, guid)

        } else {
            Log.e(this.javaClass.simpleName, "No items")
        }



//        val tag = "PresenterDebugging"
//        Log.d(tag, "Position is $position, item is ${DataInjector.getItemInPosition(position)}")
    }

    override fun refreshData() {
        refreshDataDisposable = dataProvider!!.getRefreshDataObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableObserver<List<Item>>() {
                    override fun onComplete() {
                    }

                    override fun onNext(t: List<Item>) {
//                        Log.d(logTag, "onNext in refreshData is triggered")
                        view?.insertNewItems(t)
                        val message = if(t.size == 1) {
                            "${t.size} new item received."
                        }
                        else {
                            "${t.size} new items received."
                        }
                        view?.showSnackbar(message)


                    }

                    override fun onError(e: Throwable) {
                        Log.e(logTag, "Error in refreshData {${e.message}")
                        view?.showSnackbar("Error: can't refresh data.")
                    }
                })
    }
}