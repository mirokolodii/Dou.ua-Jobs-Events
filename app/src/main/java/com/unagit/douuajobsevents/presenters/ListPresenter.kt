package com.unagit.douuajobsevents.presenters

import android.os.Handler
import android.util.Log
import androidx.paging.PagedList
import com.unagit.douuajobsevents.contracts.ListContract
import com.unagit.douuajobsevents.helpers.ItemType
import com.unagit.douuajobsevents.helpers.Messages
import com.unagit.douuajobsevents.models.Item
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.observers.DisposableObserver
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
                view?.showMessage(Messages.REFRESH_NO_NETWORK_MESSAGE)
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
        refreshData()
    }

    override fun getEvents() {
        getItems(ItemType.EVENT)
    }

    override fun getVacancies() {
        getItems(ItemType.JOB)
    }

    override fun getFavourites() {
        getItems(ItemType.FAV)
    }

    private fun getItems(type: ItemType) {
        view?.showLoading(true)
        disposables.clear()
        val observable = when (type) {
            ItemType.EVENT -> dataProvider.getEventsObservable()
            ItemType.JOB -> dataProvider.getVacanciesObservable()
            ItemType.FAV -> dataProvider.getPagedFavouritesObservable()
        }

        val observer = observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<PagedList<Item>>() {
                    override fun onNext(t: PagedList<Item>) {
                        view?.showItems(t)
                        view?.showLoading(false)
                    }

                    override fun onComplete() {
                        Log.e("Paging", "getPagedItems onComplete")
                    }

                    override fun onError(e: Throwable) {
                        view?.showLoading(false)
                        e.printStackTrace()
                        view?.showMessage(Messages.LOCAL_ITEMS_GET_ERROR_MESSAGE)
                    }
                })
        disposables.add(observer)
    }

    /**
     * Asks Data provider to refresh a data from web.
     * Inserts new items into view's list and shows snackbar message with
     * a number of newly received items.
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