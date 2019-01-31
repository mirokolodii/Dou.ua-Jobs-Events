package com.unagit.douuajobsevents.presenters

import com.google.common.truth.Truth.assertThat
import com.unagit.douuajobsevents.contracts.ListContract
import com.unagit.douuajobsevents.helpers.ItemType
import com.unagit.douuajobsevents.models.DataProvider
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

//@RunWith(MockitoJUnitRunner::class)
class ListPresenterTest {

    private lateinit var presenter: ListPresenter
//    private lateinit var view: ListContract.ListView

    @Before
    fun setUp() {
        System.out.println("init triggered")
        val mockView = mock(ListContract.ListView::class.java)
        val mockDataProvider = mock(DataProvider::class.java)
        presenter = spy(ListPresenter(mockView, mockDataProvider))
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

    @Test
    fun `getEvents should trigger getItems with ItemType equal to Event`() {
        presenter.getEvents()
        val type = ItemType.EVENT

        fail("test failed")

//        verify(presenter, times(1)).getItems
    }
}