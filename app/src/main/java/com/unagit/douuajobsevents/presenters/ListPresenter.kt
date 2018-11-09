package com.unagit.douuajobsevents.presenters

import android.app.Application
import android.util.Log
import com.unagit.douuajobsevents.contracts.ListContract
import com.unagit.douuajobsevents.models.Item
import com.unagit.douuajobsevents.models.DataInjector
import com.unagit.douuajobsevents.models.DataProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class ListPresenter : ListContract.ListPresenter {
    private var view: ListContract.ListView? = null
    private var dataProvider: DataProvider? = null
    private lateinit var disposable: Disposable

    override fun attach(view: ListContract.ListView, application: Application) {
        this.view = view
        this.dataProvider = DataProvider(application)
    }

    override fun detach() {
        this.view = null
        disposable.dispose()

        // TODO: Should I detach in DataProvider?
        // -- yes: lost singleton
        // -- no: memory leakage?
//        dataProvider?.detach()
    }


    override fun getItems() {
        view?.showLoading(true)

        val itemsObservable = dataProvider!!.getItemsObservable()

        disposable = itemsObservable
                .delay(500, TimeUnit.MILLISECONDS)
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
                    }
                })


    }

    override fun itemClicked(position: Int) {
        val guid = dataProvider?.getGuidForItemIn(position)

        if(guid != null) {
        view?.showItemDetails(position, guid)

        } else {
            Log.e(this.javaClass.simpleName, "No items")
        }



//        val tag = "PresenterDebugging"
//        Log.d(tag, "Position is $position, item is ${DataInjector.getItemInPosition(position)}")
    }
}