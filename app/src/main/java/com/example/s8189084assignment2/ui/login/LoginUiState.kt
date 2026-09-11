package com.example.s8189084assignment2.ui.login

// Screen state for LoginFragment. The ViewModel owns this, the Fragment only renders it.
data class LoginUiState(
    val isLoading: Boolean = false,
    // studentIdError and passwordError are for local field problems (empty, wrong format).
    // errorMessage is only for the server rejecting the request (wrong credentials, no connection).
    val studentIdError: String? = null,
    val passwordError: String? = null,
    val errorMessage: String? = null,
    val keypass: String? = null
)
