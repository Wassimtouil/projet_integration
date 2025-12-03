package com.example.myapplication.ViewModelTest.Fragment

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.myapplication.Model.Business.IRepository.ICityRepository
import com.example.myapplication.ViewModel.Fragment.Step1VM
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
class Step1VMTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: Step1VM
    private lateinit var repository: ICityRepository

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mock()
        viewModel = Step1VM(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // Test 1 : searchCity retourne une liste de villes
    @Test
    fun `searchCity sets city LiveData`() = runTest {
        val prefix = "Par"
        val apiKey = "fake_api_key"
        val fakeCities = listOf("Paris", "Parma", "Park City")

        whenever(repository.getCities(prefix, apiKey)).thenReturn(fakeCities)

        viewModel.searchCity(prefix, apiKey)
        testDispatcher.scheduler.advanceUntilIdle()

        val result = viewModel.city.getOrAwaitValue()
        assert(result.size == 3)
        assert(result.contains("Paris"))
        assert(result.contains("Parma"))
        assert(result.contains("Park City"))
    }

    // Test 2 : searchCity retourne liste vide si aucune ville
    @Test
    fun `searchCity returns empty list when no cities found`() = runTest {
        val prefix = "Xyz"
        val apiKey = "fake_api_key"

        whenever(repository.getCities(prefix, apiKey)).thenReturn(emptyList())

        viewModel.searchCity(prefix, apiKey)
        testDispatcher.scheduler.advanceUntilIdle()

        val result = viewModel.city.getOrAwaitValue()
        assert(result.isEmpty())
    }
}
