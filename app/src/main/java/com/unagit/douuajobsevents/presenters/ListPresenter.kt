package com.unagit.douuajobsevents.presenters

import android.os.Handler
import com.unagit.douuajobsevents.contracts.ListContract
import com.unagit.douuajobsevents.helpers.Messages
import com.unagit.douuajobsevents.helpers.ItemType
import com.unagit.douuajobsevents.models.DataProvider
import com.unagit.douuajobsevents.models.Item
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.observers.DisposableObserver
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit

class ListPresenter(view: ListContract.ListView, dataProvider: DataProvider) :
        ListContract.ListPresenter,
        BasePresenter<ListContract.ListView>(view, dataProvider) {

    private var refreshRunnable: Runnable? = null

    // First refresh after 5 sec.
    private val initialRefreshDelay = TimeUnit.SECONDS.toMillis(5)
    // Refresh each 5 min.
    private val refreshInterval = TimeUnit.MINUTES.toMillis(5)
//    private val refreshHandler = Handler()

    private val timer = Timer()

    init {
//        initiateDataRefresh()

        val refreshTask = object: TimerTask() {
            override fun run() {
                initiateDataRefresh()
            }

        }

        timer.schedule(refreshTask, initialRefreshDelay, refreshInterval)

    }

//    override fun attach(view: ListContract.ListView) {
//        super.attach(view)
//    }

    override fun detach() {
        super.detach()
        stopDataRefresh()
    }

    /**
     * Executes data refresh after 'initialRefreshDelay'
     * and continue with regular refreshes with 'refreshInterval'.
     */
    private fun initiateDataRefresh() {
        if (view != null && view!!.hasNetwork()) {
            refreshData()
        } else {
            view?.showMessage(Messages.REFRESH_NO_NETWORK_MESSAGE)
        }
    }


    private fun stopDataRefresh() {
//        if (refreshRunnable != null) {
//            refreshHandler.removeCallbacksAndMessages(null)
//        }
        timer.cancel()
    }

    /**
     * Asks Data provider to delete all items from local db.
     * Informs view to show a message with a result.
     */
    override fun clearLocalData() {
        val observer = dataProvider.getDeleteLocalDataObservable()
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
        compositeDisposable.add(observer)
        refreshData()
    }

    override fun getEvents() {
        getItems(ItemType.EVENT)
    }

    override fun getVacancies() {
        getItems(ItemType.JOB)
    }

    override fun getFavourites() {
        getItems()
    }

    /**
     * Requests locally stored items from data provider and shows them in view.
     * @param type ItemType, which should be shown (either Event of Vacancy).
     * If ItemType not provided, a default value of 'null' is used, which returns
     * list of favourites.
     * @see ItemType
     */
    private fun getItems(type: ItemType? = null) {
        view?.showLoading(true)

        val observable = when (type) {
            ItemType.EVENT -> dataProvider.getEventsObservable()
            ItemType.JOB -> dataProvider.getVacanciesObservable()
            else -> dataProvider.getFavouritesObservable()
        }

        val observer = observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Item>>() {
                    override fun onSuccess(t: List<Item>) {
                        view?.showLoading(false)
                        view?.showItems(t)
                    }

                    override fun onError(e: Throwable) {
                        view?.showLoading(false)
                        e.printStackTrace()
                        view?.showMessage(Messages.LOCAL_ITEMS_GET_ERROR_MESSAGE)
                    }
                })
        compositeDisposable.add(observer)
    }

    /**
     * Asks Data provider to refresh a data from web.
     * Inserts new items into view's list and requests view to show message
     * with a count of new items.
     * @see Item
     */
    override fun refreshData() {
        view?.showLoading(true)
        val newItems = mutableListOf<Item>()
        val observer = dataProvider.getRefreshDataObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<List<Item>>() {
                    override fun onComplete() {
                        view?.showLoading(false)
                        view?.insertNewItems(newItems)
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
        compositeDisposable.add(observer)
    }

    override fun delete(item: Item, position: Int) {
        val observable = dataProvider.getDeleteItemObservable(item)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableCompletableObserver() {
                    override fun onComplete() {
                        view?.removeAt(position)
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        view?.showMessage(Messages.DELETE_ERROR_MESSAGE)
                    }
                })
        compositeDisposable.add(observable)
    }
}