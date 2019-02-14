package com.unagit.douuajobsevents.ui

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.unagit.douuajobsevents.ui.list.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest2 {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun mainActivityTest2() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(250)

        val overflowMenuButton = onView(withContentDescription("More options"))
        overflowMenuButton.perform(click())


        Thread.sleep(2000)


//        val frameLayout = onView(
//                allOf(IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java), isDisplayed()))
//        frameLayout.check(matches(isDisplayed()))
    }
}
