package com.example.s8189084assignment2.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.s8189084assignment2.data.repository.SportsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: SportsRepository
) : ViewModel() {

    // _uiState is the writable version, only this class can change it.
    // uiState is the read-only version, exposed for DashboardFragment to observe.
    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    // Set once a load starts, stops a configuration change from repeating the API call.
    private var hasLoaded = false

    // Called from the Fragment's onViewCreated, only runs the network call the first time.
    fun loadDashboard(keypass: String) {
        if (hasLoaded) return
        hasLoaded = true
        fetchDashboard(keypass)
    }

    // Called from the Retry button, always runs the network call again.
    fun retry(keypass: String) {
        fetchDashboard(keypass)
    }

    private fun fetchDashboard(keypass: String) {
        viewModelScope.launch {
            // Update the state to show loading, and clear any old error.
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                // Ask the repository for the dashboard, then save the entities and total on success.
                val response = repository.getDashboard(keypass)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        sports = response.entities,
                        entityTotal = response.entityTotal
                    )
                }
            } catch (e: HttpException) {
                // If the server rejects the request, show a generic load-failure message.
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = "Unable to load sports. Please try again.")
                }
            } catch (e: IOException) {
                // If the request never reaches the server (no internet, or the API is still waking up), show a connection error.
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = "Unable to connect. Please try again.")
                }
            } catch (e: Exception) {
                // For any other unexpected error, show a generic message instead of the raw error.
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = "Something went wrong. Please try again.")
                }
            }
        }
    }
}
