package com.unagit.douuajobsevents.contracts

import com.unagit.douuajobsevents.models.Item
import com.unagit.douuajobsevents.presenters.BasePresenter

interface ListContract {

    interface ListView {
        fun showLoading(show: Boolean)
        fun showItems(items: List<Item>)
        fun showItemDetails(position: Int, guid: String)
        fun showSnackbar(string: String)
    }

    interface ListPresenter : BasePresenter<ListView> {
        fun getItems()
        fun itemClicked(position: Int)
        fun refreshData()
    }
}
