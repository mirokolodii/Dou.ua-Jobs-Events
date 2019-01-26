package com.unagit.douuajobsevents.presenters

import com.unagit.douuajobsevents.MyApp
import io.reactivex.disposables.CompositeDisposable

abstract class BasePresenter<V> {
    protected var view: V? = null
    val dataProvider = MyApp.dataProvider!!
    protected val disposables = CompositeDisposable()
    protected val logTag: String = this.javaClass.simpleName

    open fun attach(view: V) {
        this.view = view

    }
    open fun detach() {
        dispose()
        this.view = null
    }

    private fun dispose() {
        if (!disposables.isDisposed) {
            disposables.dispose()
        }
    }
}