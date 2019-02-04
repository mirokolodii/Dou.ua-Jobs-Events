package com.unagit.douuajobsevents.presenters

import android.os.Handler
import androidx.paging.PagedList
import com.unagit.douuajobsevents.contracts.ListContract
import com.unagit.douuajobsevents.helpers.Messages
import com.unagit.douuajobsevents.helpers.Tab
import com.unagit.douuajobsevents.helpers.ItemType
import com.unagit.douuajobsevents.models.DataProvider
import com.unagit.douuajobsevents.models.Item
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class ListPresenter(view: ListContract.ListView, dataProvider: DataProvider) :
        ListContract.ListPresenter,
        BasePresenter<ListContract.ListView>(view, dataProvider) {

//    private var refreshRunnable: Runnable? = null

    // First refresh after 5 sec.
//    private val initialRefreshDelay = TimeUnit.SECONDS.toMillis(5)
//    // Refresh each 5 min.
//    private val refreshInterval = TimeUnit.MINUTES.toMillis(5)
////    private val refreshHandler = Handler()
//
//    private val timer = Timer()
//
//    init {
//        val refreshTask = object: TimerTask() {
//            override fun run() {
//                initiateDataRefresh()
//            }
//        }
//        timer.schedule(refreshTask, initialRefreshDelay, refreshInterval)
//    }

    private val minSearchLength = 3


//    override fun attach(view: ListContract.ListView) {
//        super.attach(view)
//    }

//    override fun detach() {
//        super.detach()
////        stopDataRefresh()
//    }

    /**
     * Executes data refresh after 'initialRefreshDelay'
     * and continue with regular refreshes with 'refreshInterval'.
     */
    override fun initiateDataRefresh() {
        if (view != null && view!!.hasNetwork()) {
            refresh()
        } else {
            view?.showMessage(Messages.REFRESH_NO_NETWORK_MESSAGE)
        }
    }


//    private fun stopDataRefresh() {
////        if (refreshRunnable != null) {
////            refreshHandler.removeCallbacksAndMessages(null)
////        }
//        timer.cancel()
//    }

    /**
     * Asks Data provider to delete all items from local db.
     * Informs view to show a message with a result.
     */
    override fun clearLocalData() {
        val observer = dataProvider.getDeleteLocalDataCompletable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableCompletableObserver() {
                    override fun onComplete() {
                        view?.showMessage(Messages.LOCAL_ITEMS_DELETE_SUCCESS_MESSAGE)
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        view?.showMessage(Messages.LOCAL_ITEMS_DELETE_ERROR_MESSAGE)
                    }
                })
        disposables.add(observer)
        refresh()
    }

    override fun getEvents() {
        getItems(dataProvider.getEventsObservable())
    }

    override fun getVacancies() {
        getItems(dataProvider.getVacanciesObservable())
    }

    override fun getFavourites() {
        getItems(dataProvider.getFavouritesObservable())
    }

    override fun search(value: String, tab: Tab) {
        if (value.length < minSearchLength)
            return

        when (tab) {
            Tab.EVENTS -> getItems(dataProvider.getSearchEventsObservable(value))
            Tab.VACANCIES -> getItems(dataProvider.getSearchVacanciesObservable(value))
            Tab.FAVOURITES -> getItems(dataProvider.getSearchFavObservable(value))
        }
    }

    private fun getItems(observable: Observable<PagedList<Item>>) {
        view?.showLoading(true)
        disposables.clear() // This is important -> clear all previous PagedList observers

        val observer = observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<PagedList<Item>>() {
                    override fun onNext(t: PagedList<Item>) {
                        view?.showItems(t)
                        view?.showLoading(false)
                    }

                    override fun onComplete() {
                    }

                    override fun onError(e: Throwable) {
                        view?.showLoading(false)
                        e.printStackTrace()
                        view?.showMessage(Messages.LOCAL_ITEMS_GET_ERROR_MESSAGE)
                    }
                })
        disposables.add(observer)
    }

    override fun refresh() {
        view?.showLoading(true)
        val newItems = mutableListOf<Item>()
        val observer = dataProvider.getRefreshDataObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<List<Item>>() {
                    override fun onComplete() {
                        view?.showLoading(false)
                        val message = Messages.getMessageForCount(newItems.size)
                        view?.showMessage(message)
                    }

                    override fun onNext(t: List<Item>) {
                        newItems.addAll(t)
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        view?.showMessage(Messages.REFRESH_ERROR_MESSAGE)
                        view?.showLoading(false)
                    }
                })
        disposables.add(observer)
    }

    override fun delete(item: Item, position: Int) {
        val observable = dataProvider.getDeleteItemCompletable(item)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableCompletableObserver() {
                    override fun onComplete() {
                    }
                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        view?.showMessage(Messages.DELETE_ERROR_MESSAGE)
                    }
                })
        disposables.add(observable)
    }
}