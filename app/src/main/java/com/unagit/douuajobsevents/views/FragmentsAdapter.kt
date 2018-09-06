package com.unagit.douuajobsevents.views

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.unagit.douuajobsevents.helpers.Constants

public class FragmentsAdapter(val fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return Fragment()
    }

    override fun getCount(): Int {
        return Constants.TABS_COUNT
    }


}