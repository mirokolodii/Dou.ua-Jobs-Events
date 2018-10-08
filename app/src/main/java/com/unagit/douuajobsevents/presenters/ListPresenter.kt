package com.unagit.douuajobsevents.presenters

import com.unagit.douuajobsevents.data.Item
import com.unagit.douuajobsevents.helpers.ItemType
import java.util.*
import java.util.concurrent.TimeUnit

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
        TimeUnit.SECONDS.sleep(5)
        val items = getTestItems()
        view?.showItems(items)

    }

    private fun getTestItems(): List<Item> {
        return listOf(
                Item("http://dou-pl.com", "article 1", "guid1", Date(), ItemType.EVENT),
                Item("http://dou.com", "article 2", "guid2", Date(), ItemType.JOB)
        )
    }

    interface ListView {
        fun showLoading()
        fun showItems(items: List<Item>)
    }

}