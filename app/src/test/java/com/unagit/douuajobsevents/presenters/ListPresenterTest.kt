package com.unagit.douuajobsevents.presenters

import com.google.common.truth.Truth.assertThat
import com.unagit.douuajobsevents.contracts.ListContract
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

//@RunWith(MockitoJUnitRunner::class)
class ListPresenterTest {


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
        val presenter = spy(ListPresenter::class.java)
        val view = mock(ListContract.ListView::class.java)
        presenter.attach(view)
//        verify(presenter, times(1)).
    }
}