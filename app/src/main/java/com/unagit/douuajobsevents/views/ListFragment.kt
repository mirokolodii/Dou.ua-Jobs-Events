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
import com.unagit.douuajobsevents.data.Item
import com.unagit.douuajobsevents.presenters.ListPresenter


class ListFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView: View = inflater.inflate(R.layout.fragment_list, container, false)
        Toast.makeText(context, "On create view is triggered", Toast.LENGTH_SHORT).show()
        return rootView
    }

}