package com.unagit.douuajobsevents.contracts

import androidx.paging.PagedList
import com.unagit.douuajobsevents.models.Item

/**
 * Contract between main view, listing all available items,
 * and corresponding presenter.
 * @version %I%
 * @author Myroslav Kolodii
 */
interface ListContract {
    // View
    interface ListView {
        /**
         * Informs user that some loading work is ongoing in background
         * by showing some kind of loading spinner or in any other way.
         * @param show true to show loading process, false to dismiss it.
         */
        fun showLoading(show: Boolean)

        /**
         * Shows a list of items to the user.
         * @param items List of items to be shown.
         * @see Item
         */
        fun showItems(items: PagedList<Item>)

        /**
         * Displays a message in a snackbar to the user.
         * @param text to be shown.
         */
        fun showMessage(text: String)

        /**
         * Verifes whether or not a network connection is available.
         * @return true in case network is available, false otherwise.
         */
        fun hasNetwork(): Boolean

        fun onSwiped(position: Int)
    }

    // Presenter
    interface ListPresenter {
        fun attach(view: ListView)
        fun detach()
        /**
         * Initiates a call to repository to get a list of items.
         */
//        fun getItems()
        fun getEvents()
        fun getVacancies()
        fun getFavourites()

        /**
         * Initiates refreshment process.
         */
        fun refreshData()

        /**
         * Initiates a process to clear all locally stored data.
         */
        fun clearLocalData()

        fun delete(item: Item, position: Int)
    }

    interface Refresher {
        fun scheduleRefresh()
        fun stopRefresh()
    }
}
