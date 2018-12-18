package com.unagit.douuajobsevents.presenters

import android.app.Application
import android.util.Log
import com.unagit.douuajobsevents.MyApp
import com.unagit.douuajobsevents.contracts.DetailsContract
import com.unagit.douuajobsevents.models.DataProvider
import com.unagit.douuajobsevents.models.Item
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class DetailsPresenter : DetailsContract.DetailsPresenter {

    private val compositeDisposable = CompositeDisposable()

//    private var view: DetailsContract.DetailsView? = null
//    private var dataProvider: DataProvider? = null
//
//    override fun attach(view: DetailsContract.DetailsView) {
//        this.view = view
//        this.dataProvider = MyApp.dataProviderInstance
//    }

    override fun detach() {
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
        this.view = null
    }

    /**
     * Asks Data provider for an Item with provided item guid.
     * Shows Item in a view.
     * @param id guid of an Item to be received.
     */
    override fun requestItemFromId(id: String) {
        val observer = object : DisposableSingleObserver<Item>() {
            override fun onSuccess(item: Item) {
                view?.showItem(item)
                dispose()
            }

            override fun onError(e: Throwable) {
                Log.e(
                        this.javaClass.simpleName,
                        e.message
                )
            }
        }

        val single = dataProvider!!
                .getItemWithIdObservable(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observer)

        compositeDisposable.add(single)
    }

    /**
     * Requests DataProvider to reverse favourites value.
     * Once done, tells view to show a result.
     * @param item which should be altered
     */
    override fun changeItemFavVal(item: Item) {
        val newFavValue = !item.isFavourite

        val observable = dataProvider!!.changeItemFavourite(newFavValue, item.guid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableCompletableObserver() {
                    override fun onComplete() {
                        view?.showAsFavourite(newFavValue)
                        val message = when (newFavValue) {
                            true -> "Item has been added to favourites"
                            else -> "Item has been removed from favourites"
                        }
                        view?.showMessage(message)
                    }

                    override fun onError(e: Throwable) {
                        // Internal error, so we need to throw (and fix)
                        throw(e)
                    }
                })
        compositeDisposable.add(observable)
    }
}