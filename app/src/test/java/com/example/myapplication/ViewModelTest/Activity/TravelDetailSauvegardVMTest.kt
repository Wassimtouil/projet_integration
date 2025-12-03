package com.example.myapplication.ViewModelTest.Activity

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.myapplication.Model.Business.IRepository.ITripRepository
import com.example.myapplication.Model.Entity.TripEntity
import com.example.myapplication.ViewModel.Activity.TravelDetailSauvegardVM
import com.example.myapplication.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class TravelDetailSauvegardVMTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: TravelDetailSauvegardVM
    private lateinit var repository: ITripRepository

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mock()
        viewModel = TravelDetailSauvegardVM(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // Test 1 : getTravel parse correctement le plan et met à jour la LiveData
    @Test
    fun `getTravel sets item LiveData correctly`() = runTest {
        val travelId = 1

        // Fake JSON pour PlanParser
        val planJson = """
            {
              "destination":"Paris",
              "days":[
                {"day":1,"items":[{"title":"Eiffel Tower","details":"Visite","image_query":"Eiffel Tower","image_url":null,"localisation":"Paris"}]}
              ]
            }
        """.trimIndent()

        val fakeTrip = TripEntity(
            id = 1,
            distination = "Paris",
            planJson = planJson,
            user_id = 1
        )
        whenever(repository.getTravel(travelId)).thenReturn(fakeTrip)

        viewModel.getTravel(travelId)
        testDispatcher.scheduler.advanceUntilIdle()

        val result = viewModel.item.getOrAwaitValue()
        assert(result.size == 1)

        val firstItem = result.first()
        assert(firstItem.day == 1)
        assert(firstItem.item.title == "Eiffel Tower")
        assert(firstItem.item.localisation == "Paris")
    }


}
