package com.unagit.douuajobsevents.presenters

import android.os.Handler
import com.unagit.douuajobsevents.models.Item
import com.unagit.douuajobsevents.helpers.ItemType
import com.unagit.douuajobsevents.models.DataInjector
import java.util.*

class ListPresenter {
    private var view: ListPresenter.ListView? = null

    fun attach(view: ListView) {
        this.view = view
    }

    fun detach() {
        this.view = null
    }

    fun getItems() {
        view?.showLoading()

        // Simulate network delay
        val handler = Handler()
        val runnable = Runnable {
            val items = getTestItems()
            view?.showItems(items)
        }
        handler.postDelayed(runnable, 1 * 1000)
    }

    private fun getTestItems(): List<Item> {
        return DataInjector.getItems()
    }

    interface ListView {
        fun showLoading()
        fun showItems(items: List<Item>)
    }

}