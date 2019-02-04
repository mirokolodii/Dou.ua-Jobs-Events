package com.unagit.douuajobsevents.presenters

import com.unagit.douuajobsevents.models.DataProvider
import io.reactivex.disposables.CompositeDisposable

abstract class BasePresenter<V>(protected var view: V?, val dataProvider: DataProvider) {

    protected val disposables = CompositeDisposable()
    protected val logTag: String = this.javaClass.simpleName

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