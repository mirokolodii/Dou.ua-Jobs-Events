package com.unagit.douuajobsevents.views

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import android.util.Log
import com.unagit.douuajobsevents.helpers.TabsObj

class FragmentsAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        when(position) {
            TabsObj.Tabs.TAB_1.pos -> return ListFragment()
            TabsObj.Tabs.TAB_2.pos -> return ListFragment()

            else -> {
                Log.e(this.javaClass.simpleName, "Unhandled tab position in FragmentsAdapter.kt.")
            }
        }
        return ListFragment()

    }

    override fun getCount(): Int {
        return TabsObj.TABS_COUNT
    }


}