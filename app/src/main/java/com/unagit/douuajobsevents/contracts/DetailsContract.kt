package com.unagit.douuajobsevents.contracts

import com.unagit.douuajobsevents.models.Item

/**
 * Contract between DetailsView and DetailsPresenter.
 * @version %I%
 * @author Myroslav Kolodii
 */
interface DetailsContract {

    // View
    interface DetailsView {
        /**
         * Shows item to user.
         * @param item to be shown.
         * @see Item
         */
        fun showItem(item: Item)

        fun showAsFavourite(isFavourite: Boolean)

        fun showMessage(text: String)
    }

    // Presenter
    interface DetailsPresenter {
        fun attach(view: DetailsView)
        fun detach()
        fun requestItemFromId(id: String)
        fun changeItemFavVal(item: Item)
    }
}