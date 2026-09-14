package com.example.s8189084assignment2.ui.login

import com.example.s8189084assignment2.MainDispatcherRule
import com.example.s8189084assignment2.data.model.LoginResponse
import com.example.s8189084assignment2.data.repository.SportsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import java.io.IOException
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test
import retrofit2.HttpException

class LoginViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = mockk<SportsRepository>()
    private val viewModel = LoginViewModel(repository)

    @Test
    fun `login with correct credentials stores the returned keypass`() = runTest {
        coEvery { repository.login("8189084", "Arya Suwegatama") } returns LoginResponse("sports")

        viewModel.login("8189084", "Arya Suwegatama")

        assertEquals("sports", viewModel.uiState.value.keypass)
        assertNull(viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `login with wrong credentials shows an invalid credentials message`() = runTest {
        coEvery { repository.login(any(), any()) } throws mockk<HttpException>()

        viewModel.login("8189084", "wrongpassword")

        assertEquals("Invalid credentials. Please try again.", viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `login with no network shows a connection error message`() = runTest {
        coEvery { repository.login(any(), any()) } throws IOException()

        viewModel.login("8189084", "Arya Suwegatama")

        assertEquals("Unable to connect. Please try again.", viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `login with blank fields shows field errors and skips the network call`() = runTest {
        viewModel.login("", "")

        assertEquals("Student ID is required.", viewModel.uiState.value.studentIdError)
        assertEquals("Password is required.", viewModel.uiState.value.passwordError)
        coVerify(exactly = 0) { repository.login(any(), any()) }
    }
}
