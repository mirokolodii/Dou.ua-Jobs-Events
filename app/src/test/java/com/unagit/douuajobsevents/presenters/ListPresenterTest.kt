package com.unagit.douuajobsevents.presenters

import android.util.Log
import com.google.common.truth.Truth.assertThat
import com.unagit.douuajobsevents.contracts.ListContract
import com.unagit.douuajobsevents.models.DataProvider
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

//@RunWith(MockitoJUnitRunner::class)
class ListPresenterTest {

    private lateinit var presenter: ListPresenter

    @Before
    fun init() {
        val view = mock(ListContract.ListView::class.java)
        val dataProvider = mock(DataProvider::class.java)
        presenter = spy(ListPresenter(view, dataProvider))
        Log.d("test", "init triggered with @Before")

    }




    @Test
    fun `sum of 1 and 2 should equal 3`() {
        val sum = 1 + 2
        assertThat(sum).isEqualTo(3)
    }

    @Test(expected = RuntimeException::class)
    fun `divide by zero should throw RuntimeException`() {
        divide(1,0)
    }

    private fun divide(num: Int, num2: Int): Int {
        return num / num2
    }

    @Test
    fun `attach should initiate data refresh`() {

//        presenter.attach(view)
//        verify(presenter, times(1)).
    }
}