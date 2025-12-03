package com.example.myapplication.ViewModelTest.Activity

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.myapplication.Model.Business.IRepository.IEditProfileRepository
import com.example.myapplication.Model.Entity.UserEntity
import com.example.myapplication.ViewModel.Activity.EditProfileVM
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
class EditProfileVMTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: EditProfileVM
    private lateinit var repo: IEditProfileRepository

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repo = mock()
        viewModel = EditProfileVM(repo)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // Test 1 : loadUser récupère l'utilisateur
    @Test
    fun `loadUser sets user LiveData`() = runTest {
        val fakeUser = UserEntity(1, "Wassim", "test@mail.com", "123456")
        whenever(repo.findUserById(1)).thenReturn(fakeUser)

        viewModel.loadUser(1)
        testDispatcher.scheduler.advanceUntilIdle()

        val result = viewModel.user.getOrAwaitValue()
        assert(result == fakeUser)
    }

    // Test 2 : updateUser succès
    @Test
    fun `updateUser sets updatedUser to true when update succeeds`() = runTest {
        val fakeUser = UserEntity(1, "Wassim", "test@mail.com", "123456")
        whenever(repo.findUserById(1)).thenReturn(fakeUser)
        whenever(repo.updateUser(fakeUser)).thenReturn(1)

        viewModel.updateUser(1, "NewName", "new@mail.com", "newpass","")
        testDispatcher.scheduler.advanceUntilIdle()

        val updated = viewModel.updatedUser.getOrAwaitValue()
        assert(updated)

        // Vérifier que les champs ont été modifiés
        val updatedUser = viewModel.user.getOrAwaitValue()
        assert(updatedUser.username == "NewName")
        assert(updatedUser.email == "new@mail.com")
        assert(updatedUser.password == "newpass")
    }

    // Test 3 : updateUser échec
    @Test
    fun `updateUser sets updatedUser to false when update fails`() = runTest {
        val fakeUser = UserEntity(1, "Wassim", "test@mail.com", "123456")
        whenever(repo.findUserById(1)).thenReturn(fakeUser)
        whenever(repo.updateUser(fakeUser)).thenReturn(0)

        viewModel.updateUser(1, "NewName", "new@mail.com", "newpass","")
        testDispatcher.scheduler.advanceUntilIdle()

        val updated = viewModel.updatedUser.getOrAwaitValue()
        assert(!updated)
    }
}
