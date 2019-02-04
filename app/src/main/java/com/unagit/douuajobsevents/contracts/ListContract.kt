package com.unagit.douuajobsevents.contracts

import androidx.paging.PagedList
import com.unagit.douuajobsevents.helpers.Tab
import com.unagit.douuajobsevents.models.Item

interface ListContract {
    interface ListView {
        fun showLoading(show: Boolean)
        fun showItems(items: PagedList<Item>)
        fun showMessage(text: String)
        fun hasNetwork(): Boolean
        fun onSwiped(position: Int)
    }

    interface ListPresenter {
        fun detach()
        fun getEvents()
        fun getVacancies()
        fun getFavourites()
        fun initiateDataRefresh()
        fun clearLocalData()
        fun search(value: String, tab: Tab)
        fun delete(item: Item, position: Int)
    }

    interface Refresher {
        fun scheduleRefresh()
        fun stopRefresh()
    }
}
