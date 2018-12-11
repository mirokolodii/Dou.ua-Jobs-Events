package com.unagit.douuajobsevents.contracts

import com.unagit.douuajobsevents.helpers.ItemType
import com.unagit.douuajobsevents.models.Item
import com.unagit.douuajobsevents.presenters.BasePresenter

interface ListContract {

    interface ListView {
        fun showLoading(show: Boolean)
        fun showItems(items: List<Item>)
        fun showSnackbar(string: String)
        fun hasNetwork(): Boolean
        fun insertNewItems(newItems: List<Item>)
    }

    interface ListPresenter : BasePresenter<ListView> {
        fun getItems(type: ItemType)
        fun getFavourites()
        fun refreshData()
        fun clearLocalData(type: ItemType?)
    }
}
