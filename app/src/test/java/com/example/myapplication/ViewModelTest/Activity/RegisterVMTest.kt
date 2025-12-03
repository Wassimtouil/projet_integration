package com.example.myapplication.ViewModelTest.Activity

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.myapplication.Model.Business.IRepository.IUserRepository
import com.example.myapplication.Model.Entity.UserEntity
import com.example.myapplication.ViewModel.Activity.RegisterVM
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
class RegisterVMTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: RegisterVM
    private lateinit var repository: IUserRepository

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mock()
        // Validator simple pour tests JVM
        viewModel = RegisterVM(repository) { email -> email.contains("@") }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // Test 1 : champs vides
    @Test
    fun `register fails when fields are empty`() = runTest {
        val user = UserEntity(0, "", "", "")
        viewModel.register(user)
        testDispatcher.scheduler.advanceUntilIdle()

        val result = viewModel.registerResult.getOrAwaitValue()
        assert(result.failed == "Tous les champs sont obligatoires")
    }

    // Test 2 : username invalide (contient autre chose que des lettres)
    @Test
    fun `register fails when username is invalid`() = runTest {
        val user = UserEntity(0, "User123", "test@mail.com", "12345")
        viewModel.register(user)
        testDispatcher.scheduler.advanceUntilIdle()

        val result = viewModel.registerResult.getOrAwaitValue()
        assert(result.failed == "Username doit etre composé des lettres")
    }

    // Test 3 : email invalide
    @Test
    fun `register fails when email is invalid`() = runTest {
        val user = UserEntity(0, "Wassim", "abc", "12345")
        viewModel.register(user)
        testDispatcher.scheduler.advanceUntilIdle()

        val result = viewModel.registerResult.getOrAwaitValue()
        assert(result.failed == "Email est incorrect")
    }

    // Test 4 : password trop court ou caractères invalides
    @Test
    fun `register fails when password is too short or invalid`() = runTest {
        val user1 = UserEntity(0, "Wassim", "test@mail.com", "123") // trop court
        viewModel.register(user1)
        testDispatcher.scheduler.advanceUntilIdle()
        val result1 = viewModel.registerResult.getOrAwaitValue()
        assert(result1.failed == "Mot de passe doit etre de longueur >=5")

        val user2 = UserEntity(0, "Wassim", "test@mail.com", "12345!") // caractères invalides
        viewModel.register(user2)
        testDispatcher.scheduler.advanceUntilIdle()
        val result2 = viewModel.registerResult.getOrAwaitValue()
        assert(result2.failed == "Mot de passe doit etre composé de lettres ou chiffres")
    }

    // Test 5 : registration réussie
    @Test
    fun `register succeeds when repository returns positive`() = runTest {
        val user = UserEntity(0, "Wassim", "test@mail.com", "12345")

        whenever(repository.register(user)).thenReturn(1) // >0 signifie succès

        viewModel.register(user)
        testDispatcher.scheduler.advanceUntilIdle()

        val result = viewModel.registerResult.getOrAwaitValue()
        assert(result.success == user)
    }

    // Test 6 : registration échoue si repository retourne <=0
    @Test
    fun `register fails when repository returns non-positive`() = runTest {
        val user = UserEntity(0, "Wassim", "test@mail.com", "12345")

        whenever(repository.register(user)).thenReturn(0) // <=0 = échec

        viewModel.register(user)
        testDispatcher.scheduler.advanceUntilIdle()

        val result = viewModel.registerResult.getOrAwaitValue()
        assert(result.failed == "Inscription n'est pas valide")
    }
}
