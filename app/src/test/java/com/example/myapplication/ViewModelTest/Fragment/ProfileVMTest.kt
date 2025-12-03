package com.example.myapplication.ViewModelTest.Fragment

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.myapplication.Model.Business.IRepository.IProfileRepository
import com.example.myapplication.Model.Entity.UserEntity
import com.example.myapplication.ViewModel.Fragment.ProfileVM
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
class ProfileVMTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: ProfileVM
    private lateinit var repo: IProfileRepository

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repo = mock()
        viewModel = ProfileVM(repo)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // Test 1 : getUserById récupère correctement l'utilisateur
    @Test
    fun `getUserById sets user LiveData`() = runTest {
        val userId = 1
        val fakeUser = UserEntity(1, "Wassim", "test@mail.com", "123456")
        whenever(repo.getUserById(userId)).thenReturn(fakeUser)

        viewModel.getUserById(userId)
        testDispatcher.scheduler.advanceUntilIdle()

        val result = viewModel.user.getOrAwaitValue()
        assert(result == fakeUser)
    }

}
