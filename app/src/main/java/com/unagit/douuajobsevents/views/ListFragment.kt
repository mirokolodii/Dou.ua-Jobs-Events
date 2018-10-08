package com.unagit.douuajobsevents.views

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.unagit.douuajobsevents.R
import com.unagit.douuajobsevents.presenters.ListPresenter


class ListFragment : Fragment(), ListPresenter.ListView {
    private val presenter = ListPresenter()

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        presenter.attach(this)
    }


    override fun onDestroy() {
        super.onDestroy()
        presenter.detach()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView: View = inflater.inflate(R.layout.fragment_list, container, false)
        Toast.makeText(context, "On create view is triggered", Toast.LENGTH_SHORT).show()
        return rootView
    }

    override fun showLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showItems() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}