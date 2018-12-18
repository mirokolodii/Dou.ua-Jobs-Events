package com.unagit.douuajobsevents.presenters

import com.unagit.douuajobsevents.MyApp
import io.reactivex.disposables.CompositeDisposable

abstract class BasePresenter<V> {
    protected var view: V? = null
    val dataProvider = MyApp.dataProvider
    protected val compositeDisposable = CompositeDisposable()
    protected val logTag: String = this.javaClass.simpleName

    fun attach(view: V) {
        this.view = view

    }
    fun detach() {
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
        this.view = null
    }
}