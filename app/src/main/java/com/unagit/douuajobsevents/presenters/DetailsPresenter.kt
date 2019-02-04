package com.unagit.douuajobsevents.presenters

import android.util.Log
import com.unagit.douuajobsevents.contracts.DetailsContract
import com.unagit.douuajobsevents.models.Item
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class DetailsPresenter :
        DetailsContract.DetailsPresenter,
        BasePresenter<DetailsContract.DetailsView>() {

    /**
     * Requests data provider for an Item with given item guid.
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

        val single = dataProvider
                .getItemWithIdSingle(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observer)
        disposables.add(single)
    }

    /**
     * Requests data provider to reverse favourites value.
     * Once done, tells view to show a result.
     * @param item which should be altered
     */
    override fun changeItemFavVal(item: Item) {
        val newFavValue = !item.isFavourite

        val observable = dataProvider.switchFavouriteState(newFavValue, item.guid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableCompletableObserver() {
                    override fun onComplete() {
                        view?.showAsFavourite(newFavValue)
                    }

                    override fun onError(e: Throwable) {
                        // Internal error, so we need to throw (and fix)
                        throw(e)
                    }
                })
        disposables.add(observable)
    }
}