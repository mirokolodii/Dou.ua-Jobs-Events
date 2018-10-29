package com.unagit.douuajobsevents.presenters

import android.os.Handler
import android.util.Log
import com.unagit.douuajobsevents.contracts.ListContract
import com.unagit.douuajobsevents.models.Item
import com.unagit.douuajobsevents.models.DataInjector
import java.util.*

class ListPresenter : ListContract.ListPresenter {
    private var view: ListContract.ListView? = null

    override fun attach(view: ListContract.ListView) {
        this.view = view
    }

    override fun detach() {
        this.view = null
    }


    override fun getItems() {
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

    override fun itemClicked(position: Int) {
        view?.showItemDetails(
                position,
                DataInjector.getItemInPosition(position))
//        val tag = "PresenterDebugging"
//        Log.d(tag, "Position is $position, item is ${DataInjector.getItemInPosition(position)}")
    }
}