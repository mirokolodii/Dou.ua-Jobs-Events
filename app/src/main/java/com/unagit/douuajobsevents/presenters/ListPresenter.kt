package com.unagit.douuajobsevents.presenters

import android.app.Application
import android.os.Handler
import android.util.Log
import com.unagit.douuajobsevents.contracts.ListContract
import com.unagit.douuajobsevents.models.Item
import com.unagit.douuajobsevents.models.DataInjector
import com.unagit.douuajobsevents.models.DataProvider
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*

class ListPresenter : ListContract.ListPresenter {
    private var view: ListContract.ListView? = null
    private var dataProvider: DataProvider? = null

    override fun attach(view: ListContract.ListView, application: Application) {
        this.view = view
        this.dataProvider = DataProvider(application)
    }

    override fun detach() {
        this.view = null
        // TODO: Should I detach in DataProvider?
        // -- yes: lost singleton
        // -- no: memory leakage?
//        dataProvider?.detach()
    }


    override fun getItems() {
        view?.showLoading()

        val itemsObservable = dataProvider!!.getItemsObservable()

        itemsObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object: Observer<List<Item>>{
                    override fun onComplete() {
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(t: List<Item>) {
                        view?.showItems(t)
                    }

                    override fun onError(e: Throwable) {
                    }
                })



//        // Simulate network delay
//        val handler = Handler()
//        val runnable = Runnable {
//            val items = getTestItems()
//        }
//        handler.postDelayed(runnable, 1 * 1000)


    }


    private fun getTestItems(): List<Item> {
        return DataInjector.getItems()
    }

    override fun itemClicked(position: Int) {
        view?.showItemDetails(
                position,
                DataInjector.getItemInPosition(position))
//        val tag = "PresenterDebugging"
//        Log.d(tag, "Position is $position, item is ${DataInjector.getItemInPosition(position)}")
    }
}