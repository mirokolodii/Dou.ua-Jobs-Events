package com.unagit.douuajobsevents.presenters

import com.unagit.douuajobsevents.models.DataProvider
import io.reactivex.disposables.CompositeDisposable

abstract class BasePresenter<V>(val dataProvider: DataProvider) {

    protected var view: V? = null

    protected val disposables = CompositeDisposable()
    protected val logTag: String = this.javaClass.simpleName

    fun attach(view: V) {
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