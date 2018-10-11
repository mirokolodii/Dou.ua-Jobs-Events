package com.unagit.douuajobsevents.presenters

import android.os.Handler
import com.unagit.douuajobsevents.data.Item
import com.unagit.douuajobsevents.helpers.ItemType
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

        handler.postDelayed(runnable, 5 * 1000)



    }

    private fun getTestItems(): List<Item> {
        return listOf(
                Item("http://dou-pl.com", "article 1", "guid1", Date(), ItemType.EVENT,
                        "https://s.dou.ua/CACHE/images/img/events/logo_RswGD5i/a4a3a04c927fc75f27a9a7974ed652f1.png"),
                Item("http://dou.com", "article 2", "guid2", Date(), ItemType.JOB,
                        "https://s.dou.ua/CACHE/images/img/events/%D0%92%D0%B5%D0%B1%D1%96%D0%BD%D0%B0%D1%80_C_17_10_%D1%80%D1%83%D1%81/8e483631e3cfb4158d56841f6e8c4280.png")
        )
    }

    interface ListView {
        fun showLoading()
        fun showItems(items: List<Item>)
    }

}