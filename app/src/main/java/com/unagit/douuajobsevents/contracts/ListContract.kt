package com.unagit.douuajobsevents.contracts

import com.unagit.douuajobsevents.models.Item
import com.unagit.douuajobsevents.presenters.BasePresenter

interface ListContract {

    interface ListView {
        fun showLoading()
        fun showItems(items: List<Item>)
        fun showDetails(position: Int, item: Item)
    }

    interface ListPresenter : BasePresenter<ListView> {
        fun getItems()
        fun itemClicked(position: Int)
    }
}
