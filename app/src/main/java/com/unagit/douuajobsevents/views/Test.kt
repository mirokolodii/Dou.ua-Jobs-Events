package com.unagit.douuajobsevents.views

import com.unagit.douuajobsevents.models.Item

import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable

class Test {

    private val observer = object : SingleObserver<Item> {
        override fun onSubscribe(d: Disposable) {

        }

        override fun onSuccess(item: Item) {

        }

        override fun onError(e: Throwable) {

        }
    }
}
