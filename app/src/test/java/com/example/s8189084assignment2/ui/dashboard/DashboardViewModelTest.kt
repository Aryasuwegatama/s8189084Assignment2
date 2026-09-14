package com.example.s8189084assignment2.ui.dashboard

import com.example.s8189084assignment2.MainDispatcherRule
import com.example.s8189084assignment2.data.model.DashboardResponse
import com.example.s8189084assignment2.data.model.Sport
import com.example.s8189084assignment2.data.repository.SportsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import java.io.IOException
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class DashboardViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = mockk<SportsRepository>()
    private val viewModel = DashboardViewModel(repository)

    private val football = Sport("Football", 11, "Grass", true, "A team sport.")
    private val cricket = Sport("Cricket", 11, "Pitch", false, "A bat-and-ball sport.")

    @Test
    fun `loadDashboard with a successful response updates the sports list and total`() = runTest {
        coEvery { repository.getDashboard("sports") } returns DashboardResponse(listOf(football, cricket), 2)

        viewModel.loadDashboard("sports")

        assertEquals(listOf(football, cricket), viewModel.uiState.value.sports)
        assertEquals(2, viewModel.uiState.value.entityTotal)
        assertNull(viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `loadDashboard with no network shows a connection error message`() = runTest {
        coEvery { repository.getDashboard("sports") } throws IOException()

        viewModel.loadDashboard("sports")

        assertEquals("Unable to connect. Please try again.", viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `loadDashboard with an empty entity list shows zero sports without an error`() = runTest {
        coEvery { repository.getDashboard("sports") } returns DashboardResponse(emptyList(), 0)

        viewModel.loadDashboard("sports")

        assertTrue(viewModel.uiState.value.sports.isEmpty())
        assertEquals(0, viewModel.uiState.value.entityTotal)
        assertNull(viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `loadDashboard called twice only calls the repository once`() = runTest {
        coEvery { repository.getDashboard("sports") } returns DashboardResponse(listOf(football), 1)

        viewModel.loadDashboard("sports")
        viewModel.loadDashboard("sports")

        coVerify(exactly = 1) { repository.getDashboard("sports") }
    }
}
