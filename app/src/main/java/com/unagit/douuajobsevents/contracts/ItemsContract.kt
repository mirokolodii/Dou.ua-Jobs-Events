package com.unagit.douuajobsevents.contracts

interface SearchItemsContract {

    interface View {

    }

    interface Presenter {
        fun load()
        fun attach()
        fun detach()
    }
}