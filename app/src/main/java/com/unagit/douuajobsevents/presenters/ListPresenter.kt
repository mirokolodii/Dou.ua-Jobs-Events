package com.unagit.douuajobsevents.presenters

import android.app.Application
import android.os.Handler
import android.util.Log
import com.unagit.douuajobsevents.contracts.ListContract
import com.unagit.douuajobsevents.helpers.ItemType
import com.unagit.douuajobsevents.models.Item
import com.unagit.douuajobsevents.models.DataProvider
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.internal.disposables.DisposableContainer
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.observers.DisposableObserver
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class ListPresenter : ListContract.ListPresenter {
    private var view: ListContract.ListView? = null
    private val compositeDisposable = CompositeDisposable()

    private var dataProvider: DataProvider? = null
//    private var localDataDisposable: Disposable? = null
//    private var clearLocalDataDisposable: Disposable? = null
//    private var refreshDataDisposable: Disposable? = null
    private var refreshRunnable: Runnable? = null
    private val initialRefreshInterval = 5 * 1000L /* 5 sec */

    private val logTag = "ListPresenter"

    // Fields used for data refresh from web with interval 'refreshInterval' msec.
    private val refreshInterval = 60 * 5 * 1000L /* 5 min */
    private val refreshHandler = Handler()

    override fun attach(view: ListContract.ListView, application: Application) {
        this.view = view
        this.dataProvider = DataProvider(application)
        initiateDataRefresh()
    }


    override fun detach() {
        stopDataRefresh()
        if(!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
//        localDataDisposable?.dispose()
//        refreshDataDisposable?.dispose()
//        clearLocalDataDisposable?.dispose()
        this.view = null

        // TODO: Should I detach in DataProvider?
        // -- yes: lost singleton
        // -- no: memory leakage?
//        dataProvider?.detach()

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

    override fun clearLocalData() {
        val clearLocalDataDisposable = dataProvider!!.getDeleteLocalDataObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableCompletableObserver() {
                    override fun onComplete() {
                        view?.showSnackbar("Local data has been deleted.")
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        view?.showSnackbar(
                                "Oops :-( Error happened while trying to delete local cache.")
                    }
                })
        compositeDisposable.add(clearLocalDataDisposable)
        getItems()
        initiateDataRefresh()
    }


    override fun getItems() {
        view?.showLoading(true)

        val localDataDisposable = dataProvider!!.getItemsObservable()
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
                        view?.showLoading(false)
                        Log.e(logTag, "Error in getItems. ${e.message}")
                        view?.showSnackbar("Error: can't receive data from local cache.")
                    }

                })

        compositeDisposable.add(localDataDisposable)
    }

    override fun getItems(type: ItemType) {
        view?.showLoading(true)

        val disposable = dataProvider!!.getItemsObservable(type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableSingleObserver<List<Item>>() {
                    override fun onSuccess(t: List<Item>) {
                        view?.showLoading(false)
                        view?.showItems(t)
                    }

                    override fun onError(e: Throwable) {
                        view?.showLoading(false)
                    }
                })
        compositeDisposable.add(disposable)
    }

    override fun refreshData() {
        val refreshDataDisposable = dataProvider!!.getRefreshDataObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableObserver<List<Item>>() {
                    override fun onComplete() {
                    }

                    override fun onNext(t: List<Item>) {
//                        Log.d(logTag, "onNext in refreshData is triggered")
                        view?.insertNewItems(t)
                        val message = when {
                            t.isEmpty() -> "No new items."
                            t.size == 1 -> "${t.size} new item received."
                            else -> "${t.size} new items received."
                        }
                        view?.showSnackbar(message)
                    }

                    override fun onError(e: Throwable) {
                        Log.e(logTag, "Error in refreshData. ${e.message}")
                        view?.showSnackbar("Error - can't refresh data.")
                    }
                })
        compositeDisposable.add(refreshDataDisposable)
    }
}