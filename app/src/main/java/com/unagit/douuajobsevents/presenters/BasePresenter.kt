package com.unagit.douuajobsevents.presenters

import android.app.Application

interface BasePresenter<V> {
    fun attach(view: V, application: Application)
    fun detach()
}