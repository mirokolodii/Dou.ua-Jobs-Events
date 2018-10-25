package com.unagit.douuajobsevents.presenters

interface BasePresenter<V> {
    fun attach(view: V)
    fun detach()
}