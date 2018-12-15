package com.unagit.douuajobsevents.presenters

import android.app.Application
import android.util.Log
import com.unagit.douuajobsevents.contracts.DetailsContract
import com.unagit.douuajobsevents.models.DataProvider
import com.unagit.douuajobsevents.models.Item
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class DetailsPresenter : DetailsContract.DetailsPresenter {

    private var view: DetailsContract.DetailsView? = null
    private var dataProvider: DataProvider? = null
    private val compositeDisposable = CompositeDisposable()

    override fun attach(view: DetailsContract.DetailsView, application: Application) {
        this.view = view
        this.dataProvider = DataProvider(application)
    }

    override fun detach() {
        if(!compositeDisposable.isDisposed) {
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
     * Sets Item with provided guid as favourite.
     * @param guid of Item to be altered
     * @see Item
     */
    override fun addToFavourites(guid: String) {
        setAs(true, guid)
    }

    /**
     * Removes Item with provided guid from favourites.
     * @param guid of Item to be altered
     * @see Item
     */
    override fun removeFromFavourites(guid: String) {
        setAs(false, guid)
    }

    /**
     * Asks DataProvider to set or remove from favourites.
     * Once done, tells view to show a result.
     * @param favourite true - set as favourite, false - remove from favourites
     * guid of Item to be altered
     */
    private fun setAs(favourite: Boolean, guid: String) {
        //TODO: get Completable from dataProvider and show result in view
        dataProvider!!.changeItemFavourite(favourite, guid)
    }
}