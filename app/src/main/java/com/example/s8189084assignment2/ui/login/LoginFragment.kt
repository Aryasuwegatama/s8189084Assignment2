package com.example.s8189084assignment2.ui.login

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.s8189084assignment2.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private val viewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get references to every view this Fragment needs to read from or update.
        val studentIdLayout = view.findViewById<TextInputLayout>(R.id.studentIdLayout)
        val studentIdInput = view.findViewById<TextInputEditText>(R.id.studentIdInput)
        val passwordLayout = view.findViewById<TextInputLayout>(R.id.passwordLayout)
        val passwordInput = view.findViewById<TextInputEditText>(R.id.passwordInput)
        val loginButton = view.findViewById<MaterialButton>(R.id.loginButton)
        val loadingIndicator = view.findViewById<LinearProgressIndicator>(R.id.loadingIndicator)
        val errorContainer = view.findViewById<MaterialCardView>(R.id.errorContainer)
        val errorText = view.findViewById<TextView>(R.id.errorText)

        // When LOGIN is tapped, read both fields and hand them to the ViewModel.
        loginButton.setOnClickListener {
            val username = studentIdInput.text?.toString().orEmpty()
            val password = passwordInput.text?.toString().orEmpty()
            viewModel.login(username, password)
        }

        // Watch the ViewModel's state and update the screen whenever it changes.
        // repeatOnLifecycle(STARTED) pauses this while the screen isn't visible.
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    // Show or hide the loading indicator, and disable the button while loading.
                    loginButton.isEnabled = !state.isLoading
                    loadingIndicator.visibility = if (state.isLoading) View.VISIBLE else View.GONE

                    // Swap the button text between normal and in-progress wording.
                    loginButton.text = getString(
                        if (state.isLoading) R.string.login_button_loading else R.string.login_button
                    )

                    // Show each field's own error under it, or clear it if there isn't one.
                    studentIdLayout.error = state.studentIdError
                    passwordLayout.error = state.passwordError

                    // If there is an error message, show it, otherwise keep the error box hidden.
                    if (state.errorMessage != null) {
                        errorText.text = state.errorMessage
                        errorContainer.visibility = View.VISIBLE
                    } else {
                        errorContainer.visibility = View.GONE
                    }

                    // If login succeeded, navigate to Dashboard and pass the keypass along.
                    if (state.keypass != null) {
                        val action = LoginFragmentDirections
                            .actionLoginFragmentToDashboardFragment(state.keypass)
                        findNavController().navigate(action)
                        viewModel.consumeKeypass()
                    }
                }
            }
        }
    }
}
