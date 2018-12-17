package com.unagit.douuajobsevents.presenters

import android.app.Application

interface BasePresenter<V> {
    // TODO why wouldn't you transfer Application object in constructor, instead of this method?
    // TODO Application will be the same in each method call.
    fun attach(view: V, application: Application)
    fun detach()
}