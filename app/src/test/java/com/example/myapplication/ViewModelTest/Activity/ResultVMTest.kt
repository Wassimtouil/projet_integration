package com.example.myapplication.ViewModelTest.Activity
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.myapplication.Model.Business.IRepository.IResultRepository
import com.example.myapplication.Model.Entity.Day
import com.example.myapplication.Model.Entity.Item
import com.example.myapplication.Model.Entity.TravelPlan
import com.example.myapplication.ViewModel.Activity.ResultVM
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
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
@OptIn(ExperimentalCoroutinesApi::class)
class ResultVMTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: ResultVM
    private lateinit var repository: IResultRepository

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mock()
        viewModel = ResultVM(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // Test 1 : génération réussie
    @Test
    fun `generatePlan sets travelPlan and updates isLoading`() = runTest {
        val prompt = "Plan voyage"
        val userId = 1
        val fakePlan = TravelPlan(
            destination = "Paris Trip",
            days = listOf(
                Day(
                    day = 1,
                    items = listOf(
                        Item(
                            title = "Eiffel Tower",
                            details = "Visite de la tour Eiffel",
                            image_query = "Eiffel Tower Paris",
                            image_url = null,
                            localisation = "Paris"
                        )
                    )
                )
            )
        )
        whenever(repository.generatePlanGemini(prompt)).thenReturn(fakePlan)
        whenever(repository.fetchImages(fakePlan)).thenReturn(Unit)
        whenever(repository.saveTrip(fakePlan, userId)).thenReturn(Unit)

        viewModel.generatePlan(prompt, userId)
        testDispatcher.scheduler.advanceUntilIdle()

        // Vérifier le loading
        assert(viewModel.isLoading.getOrAwaitValue() == false)

        // Vérifier le plan de voyage
        val resultPlan = viewModel.travelPlan.getOrAwaitValue()
        assert(resultPlan == fakePlan)

        // Vérifier qu'aucune erreur n'est déclenchée
        assert(viewModel.erreur.value == null)

        // Vérifier les interactions repository
        verify(repository).generatePlanGemini(prompt)
        verify(repository).fetchImages(fakePlan)
        verify(repository).saveTrip(fakePlan, userId)
    }

    // Test 2 : génération échoue (exception)
    @Test
    fun `generatePlan sets erreur when exception thrown`() = runTest {
        val prompt = "Plan voyage"
        val userId = 1
        val exceptionMessage = "Erreur API"

        whenever(repository.generatePlanGemini(prompt)).thenThrow(RuntimeException(exceptionMessage))

        viewModel.generatePlan(prompt, userId)
        testDispatcher.scheduler.advanceUntilIdle()

        // Vérifier le loading
        assert(viewModel.isLoading.getOrAwaitValue() == false)

        // Vérifier que travelPlan n'a pas été mis
        assert(viewModel.travelPlan.value == null)

        // Vérifier l'erreur
        val error = viewModel.erreur.getOrAwaitValue()
        assert(error == exceptionMessage)
    }
}
