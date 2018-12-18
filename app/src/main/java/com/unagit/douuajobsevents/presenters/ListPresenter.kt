package com.unagit.douuajobsevents.presenters

import android.app.Application
import android.os.Handler
import android.util.Log
import com.unagit.douuajobsevents.contracts.ListContract
import com.unagit.douuajobsevents.models.DataProvider
import com.unagit.douuajobsevents.models.Item
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class ListPresenter : ListContract.ListPresenter {
    private var view: ListContract.ListView? = null
    private var dataProvider: DataProvider? = null
    // TODO use CompositeDisposable to avoid fields boilerplate
    private var localDataDisposable: Disposable? = null
    private var clearLocalDataDisposable: Disposable? = null
    private var refreshDataDisposable: Disposable? = null
    private var refreshRunnable: Runnable? = null
    private val initialRefreshInterval = 5 * 1000L /* 5 sec */ // TODO replace with TimeUnit.SECONDS.toMillis(5)

    private val logTag = "ListPresenter"

    // Fields used for data refresh from web with interval 'refreshInterval' msec.
    private val refreshInterval = 60 * 5 * 1000L /* 5 min */ // TODO TimeUnit.MINUTES.toMillis(5)
    private val refreshHandler = Handler()

    override fun attach(view: ListContract.ListView, application: Application) {
        this.view = view
        this.dataProvider = DataProvider(application)
        initiateDataRefresh()
    }


    override fun detach() {
        stopDataRefresh()
        localDataDisposable?.dispose()
        refreshDataDisposable?.dispose()
        clearLocalDataDisposable?.dispose()
        this.view = null

        // TODO: Should I detach in DataProvider?
        // -- yes: lost singleton
        // -- no: memory leakage?
        // TODO No need to detach data providers in view detach method - presenters are made to hold these cache during view rebuilding.
        // TODO Link to dataProvider will die together with presenter itself.
        // TODO That's why i'd recommend to not create data providers in presenter, but in application object.
        // TODO In that case, you'd not have to bother about lost singleton.
//        dataProvider?.detach()

    }

    /**
     * Executes data refresh after 'initialRefreshInterval'
     * and continue with regular refreshes with 'refreshInterval'.
     */
    private fun initiateDataRefresh() {
        refreshRunnable = Runnable {
            if (view != null && view!!.hasNetwork()) {
                refreshData()
            } else {
                view?.showSnackbar("Can't refresh: no network access.")
            }
            // Regular refreshes
// TODO it's not the best (though not the worst) method to refresh data in specific period.
// TODO You may use TimerTask instead.
            refreshHandler.postDelayed(refreshRunnable, refreshInterval)
        }
        // First refresh
        refreshHandler.postDelayed(refreshRunnable, initialRefreshInterval)
    }

    private fun stopDataRefresh() {
        if (refreshRunnable != null) {
            refreshHandler.removeCallbacksAndMessages(null)
        }
    }

    /**
     * Asks Data provider to delete all items from local db.
     * Informs view to show a snackbar message with a result.
     */
    override fun clearLocalData() {
        clearLocalDataDisposable = dataProvider!!.getDeleteLocalDataObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableCompletableObserver() {
                    override fun onComplete() {
                        view?.showSnackbar("Local data has been deleted.")
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        view?.showSnackbar(
                                "Oops :-( Something went wrong while trying to delete local cache.")
                    }
                })
//        getItems()
//        initiateDataRefresh()

        // Once local data is deleted, initiate a new refresh from web.
        refreshData()
    }

    /**
     * Asks Data provider for all locally stored items
     * and shows them in view.
     * @see Item
     */
    override fun getItems() {
        view?.showLoading(true)

        val itemsObservable = dataProvider!!.getItemsObservable()

        localDataDisposable = itemsObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<List<Item>>() {
                    override fun onComplete() {
                    }

                    override fun onNext(t: List<Item>) {

                        view?.showLoading(false)
                        view?.showItems(t)
                    }

                    override fun onError(e: Throwable) {
                        Log.e(logTag, "Error in getItems. ${e.message}")
                        view?.showSnackbar("Error: can't receive data from local cache.")
                    }

                })


    }


    /**
     * Asks Data provider to refresh a data from web.
     * Inserts new items into view's list and shows snackbar message with
     * a number of newly received items.
     * @see Item
     */
    override fun refreshData() {
        refreshDataDisposable = dataProvider!!.getRefreshDataObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<List<Item>>() {
                    override fun onComplete() {
                    }

                    override fun onNext(t: List<Item>) {
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
    }
}