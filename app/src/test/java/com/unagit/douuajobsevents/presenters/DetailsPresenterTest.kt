package com.unagit.douuajobsevents.presenters

import com.unagit.douuajobsevents.contracts.DetailsContract
import com.unagit.douuajobsevents.contracts.ListContract
import com.unagit.douuajobsevents.helpers.ItemType
import com.unagit.douuajobsevents.helpers.SchedulerProvider
import com.unagit.douuajobsevents.models.DataProvider
import com.unagit.douuajobsevents.models.Item
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.mockito.Mockito.*
import java.util.*

class DetailsPresenterTest {

    private lateinit var presenter: DetailsPresenter
    private lateinit var mockView: DetailsContract.DetailsView
    private lateinit var mockDataProvider: DataProvider
    private lateinit var mockScheduler: SchedulerProvider

    @Before
    fun setUp() {
        // 1. Mock view and dataProvider
        mockView = Mockito.mock(DetailsContract.DetailsView::class.java)
        mockDataProvider = Mockito.mock(DataProvider::class.java)
        mockScheduler = mock(SchedulerProvider::class.java)

        // 2. Initialize presenter
        presenter = Mockito.spy(DetailsPresenter(mockView, mockDataProvider , mockScheduler))

        //
    }

    @Test
    fun `request for item with ID should show item in view`() {
        // 3. Create dummy item
        val item = Item("http://test.com",
                "test title",
                ItemType.EVENT.value,
                "url",
                "item description")

        // 4. Return stub
        doReturn(Single.just(item))
                .`when`(mockDataProvider).getItemWithIdObservable(ArgumentMatchers.anyString())

        doReturn(Schedulers.trampoline()).`when`(mockScheduler).io()
        doReturn(Schedulers.trampoline()).`when`(mockScheduler).ui()

        // 5. Trigger
        presenter.requestItemWithId("dummy id")

        // 6. Verify
        verify(mockView, times(1)).showItem(item)
    }

    @Test
    fun changeItemFavVal() {
    }
}