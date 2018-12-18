package com.unagit.douuajobsevents.presenters

import com.unagit.douuajobsevents.MyApp

abstract class BasePresenter<V>(var view: V?) {
    protected val logTag: String = this.javaClass.simpleName
    protected val dataProvider = MyApp.dataProviderInstance

//    abstract fun attach(view: V)
    abstract fun detach()
}