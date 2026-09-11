package com.example.s8189084assignment2.ui.login

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
class LoginViewModel @Inject constructor(
    private val repository: SportsRepository
) : ViewModel() {

    // _uiState is the writable version, only this class can change it.
    // uiState is the read-only version, exposed for LoginFragment to observe.
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(username: String, password: String) {
        // If the Student ID is empty or has a letter in it, set an error for that field.
        val studentIdError = when {
            username.isBlank() -> "Student ID is required."
            !username.all { it.isDigit() } -> "Student ID must contain numbers only."
            else -> null
        }

        // If the password is empty, set an error for that field.
        val passwordError = if (password.isBlank()) "Password is required." else null

        // If either field has a problem, show the field errors and don't call the network.
        if (studentIdError != null || passwordError != null) {
            _uiState.update {
                it.copy(studentIdError = studentIdError, passwordError = passwordError)
            }
            return
        }

        viewModelScope.launch {
            // Update the state to show logging in, and clear any old errors.
            _uiState.update {
                it.copy(
                    isLoading = true,
                    studentIdError = null,
                    passwordError = null,
                    errorMessage = null
                )
            }

            try {
                // Ask the repository to log in, then save the returned keypass if it succeeds.
                val response = repository.login(username, password)
                _uiState.update { it.copy(isLoading = false, keypass = response.keypass) }
            } catch (e: HttpException) {
                // If the server rejects the request because of wrong credentials, show the error message.
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = "Invalid credentials. Please try again.")
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

    // After LoginFragment navigates away, clear the keypass so a configuration change
    // can't trigger the navigation again.
    fun consumeKeypass() {
        _uiState.update { it.copy(keypass = null) }
    }
}
