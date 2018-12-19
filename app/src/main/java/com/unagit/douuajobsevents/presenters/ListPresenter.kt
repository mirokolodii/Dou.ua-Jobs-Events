package com.unagit.douuajobsevents.presenters

import android.os.Handler
import android.util.Log
import com.unagit.douuajobsevents.MyApp
import com.unagit.douuajobsevents.contracts.ListContract
import com.unagit.douuajobsevents.models.DataProvider
import com.unagit.douuajobsevents.models.Item
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.observers.DisposableObserver
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class ListPresenter :
        ListContract.ListPresenter,
        BasePresenter<ListContract.ListView>() {

    private var refreshRunnable: Runnable? = null

    // First refresh after 5 sec.
    private val initialRefreshInterval = TimeUnit.SECONDS.toMillis(5)
    // Refresh each 5 min.
    private val refreshInterval = TimeUnit.MINUTES.toMillis(5)
    private val refreshHandler = Handler()

    override fun attach(view: ListContract.ListView) {
        super.attach(view)
        initiateDataRefresh()
    }

    override fun detach() {
        super.detach()
        stopDataRefresh()
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
                view?.showMessage("Can't refresh: no network access.")
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
        val observable = dataProvider!!.getDeleteLocalDataObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableCompletableObserver() {
                    override fun onComplete() {
                        view?.showMessage("Local data has been deleted.")
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        view?.showMessage(
                                "Oops :-( Something went wrong while trying to delete local cache.")
                    }
                })
        compositeDisposable.add(observable)
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

        val observable = dataProvider!!.getItemsObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Item>>() {
                    override fun onSuccess(t: List<Item>) {
                        view?.showLoading(false)
                        view?.showItems(t)
                    }
                    override fun onError(e: Throwable) {
                        Log.e(logTag, "Error in getItems. ${e.message}")
                        view?.showMessage("Error: can't receive data from local cache.")
                    }
                })
        compositeDisposable.add(observable)
    }

    /**
     * Asks Data provider to refresh a data from web.
     * Inserts new items into view's list and shows snackbar message with
     * a number of newly received items.
     * @see Item
     */
    override fun refreshData() {
        val observable = dataProvider!!.getRefreshDataObservable()
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
                        view?.showMessage(message)
                    }

                    override fun onError(e: Throwable) {
                        Log.e(logTag, "Error in refreshData. ${e.message}")
                        view?.showMessage("Error - can't refresh data.")
                    }
                })
        compositeDisposable.add(observable)
    }
}