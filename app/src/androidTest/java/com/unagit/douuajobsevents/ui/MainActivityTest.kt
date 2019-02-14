package com.unagit.douuajobsevents.ui


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.unagit.douuajobsevents.R
import com.unagit.douuajobsevents.ui.list.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun mainActivityTest1() {

        val bottomNavigationItemView = onView((withId(R.id.navigation_events)))

        bottomNavigationItemView.perform(click())


//        textView.check(matches(withText("Vacancies")))
    }


}
