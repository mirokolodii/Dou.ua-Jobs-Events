package com.unagit.douuajobsevents.presenters

import android.os.Handler
import android.util.Log
import com.unagit.douuajobsevents.MyApp
import com.unagit.douuajobsevents.contracts.ListContract
import com.unagit.douuajobsevents.helpers.ItemType
import com.unagit.douuajobsevents.models.Item
import com.unagit.douuajobsevents.models.DataProvider
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.internal.disposables.DisposableContainer
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
        val observer = dataProvider.getDeleteLocalDataObservable()
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
                        Log.e(logTag, "Error in getItems. ${e.message}")
                        view?.showMessage("Error: can't receive data from local cache.")
                    }
                })
        compositeDisposable.add(observer)
    }

    /**
     * Asks Data provider to refresh a data from web.
     * Inserts new items into view's list and shows snackbar message with
     * a number of newly received items.
     * @see Item
     */
    override fun refreshData() {
        view?.showLoading(true)
        val observer = dataProvider.getRefreshDataObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<List<Item>>() {
                    override fun onComplete() {}

                    override fun onNext(t: List<Item>) {
                        view?.insertNewItems(t)
                        val message = when {
                            t.isEmpty() -> "No new items."
                            t.size == 1 -> "${t.size} new item received."
                            else -> "${t.size} new items received."
                        }
                        view?.showLoading(false)
                        view?.showMessage(message)
                    }

                    override fun onError(e: Throwable) {
                        Log.e(logTag, "Error in refreshData. ${e.message}")
                        view?.showMessage("Error - can't refresh data.")
                        view?.showLoading(false)
                    }
                })
        compositeDisposable.add(observer)
    }
}