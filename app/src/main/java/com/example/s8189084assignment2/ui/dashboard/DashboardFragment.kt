package com.example.s8189084assignment2.ui.dashboard

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.s8189084assignment2.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.progressindicator.CircularProgressIndicator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

// Shows the list of sports after login.
@AndroidEntryPoint
class DashboardFragment : Fragment(R.layout.fragment_dashboard) {

    private val viewModel: DashboardViewModel by viewModels()
    private val args: DashboardFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get references to every view this Fragment needs to read from or update.
        val subtitleText = view.findViewById<TextView>(R.id.subtitleText)
        val recyclerView = view.findViewById<RecyclerView>(R.id.sportsRecyclerView)
        val loadingIndicator = view.findViewById<CircularProgressIndicator>(R.id.loadingIndicator)
        val errorContainer = view.findViewById<MaterialCardView>(R.id.errorContainer)
        val errorText = view.findViewById<TextView>(R.id.errorText)
        val retryButton = view.findViewById<MaterialButton>(R.id.retryButton)

        val adapter = SportAdapter(onSportClick = {
            // TODO: navigate to Details with the selected sport.
        })
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        retryButton.setOnClickListener { viewModel.retry(args.keypass) }

        // Ask for the dashboard once, the ViewModel's own guard stops a repeat call after rotation.
        viewModel.loadDashboard(args.keypass)

        // Watch the ViewModel's state and update the screen whenever it changes.
        // repeatOnLifecycle(STARTED) pauses this while the screen isn't visible.
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    val showError = state.errorMessage != null
                    val showList = !state.isLoading && !showError

                    loadingIndicator.visibility = if (state.isLoading) View.VISIBLE else View.GONE
                    errorContainer.visibility = if (showError) View.VISIBLE else View.GONE
                    recyclerView.visibility = if (showList) View.VISIBLE else View.GONE

                    if (showError) {
                        errorText.text = state.errorMessage
                    }

                    subtitleText.text = getString(R.string.dashboard_subtitle, state.entityTotal)
                    adapter.submitList(state.sports)
                }
            }
        }
    }
}
