package com.example.myapplication.ViewModelTest.Activity

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.myapplication.Model.Business.IRepository.IUserRepository
import com.example.myapplication.Model.Entity.UserEntity
import com.example.myapplication.ViewModel.Activity.LoginVM
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
class LoginVMTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    private lateinit var viewModel : LoginVM
    private lateinit var repository: IUserRepository

    //pour tester les coroutines
    private val testDispatcher = StandardTestDispatcher()


    @Before
    fun setup(){
        Dispatchers.setMain(testDispatcher)
        repository=mock()
        viewModel = LoginVM(repository){email -> email.contains("@")}
    }

    @After
    fun tearDown(){
        Dispatchers.resetMain()
    }

    @Test
    fun `login fails when fields are empty`() = runTest {
        viewModel.login("","")
        testDispatcher.scheduler.advanceUntilIdle()

        val result=viewModel.loginResult.getOrAwaitValue()
        assert(result.failed == "Tous les champs sont obligatoires")
    }

    // Test 2 : email invalide
    @Test
    fun `login fails when email is invalid`() = runTest {
        viewModel.login("abc", "123456")
        testDispatcher.scheduler.advanceUntilIdle()

        val result = viewModel.loginResult.getOrAwaitValue()
        assert(result.failed == "Email est invalide")
    }

    // Test 3 : password trop court
    @Test
    fun `login fails when password is too short`() = runTest {
        viewModel.login("test@mail.com", "123")
        testDispatcher.scheduler.advanceUntilIdle()

        val result = viewModel.loginResult.getOrAwaitValue()
        assert(result.failed == "Mot de passe doit etre composé de 6 lettres et chiffres")
    }

    // Test 4 : login incorrect (repository retourne null)
    @Test
    fun `login fails when repository returns null`() = runTest {

        whenever(repository.login("test@mail.com", "123456"))
            .thenReturn(null)

        viewModel.login("test@mail.com", "123456")
        testDispatcher.scheduler.advanceUntilIdle()

        val result = viewModel.loginResult.getOrAwaitValue()
        assert(result.failed == "Email ou mot de passe est incorrect")
    }

    // Test 5 : login success
    @Test
    fun `login succeeds when repository returns user`() = runTest {

        val fakeUser = UserEntity(1, "Wassim", "test@mail.com","123456")

        whenever(repository.login(fakeUser.email,fakeUser.password))
            .thenReturn(fakeUser)

        viewModel.login(fakeUser.email,fakeUser.password)
        testDispatcher.scheduler.advanceUntilIdle()

        val result = viewModel.loginResult.getOrAwaitValue()
        assert(result.success == fakeUser)
    }
}