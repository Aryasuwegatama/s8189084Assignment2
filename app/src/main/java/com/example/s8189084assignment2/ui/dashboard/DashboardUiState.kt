package com.example.s8189084assignment2.ui.dashboard

import com.example.s8189084assignment2.data.model.Sport

// Screen state for DashboardFragment. The ViewModel owns this, the Fragment only renders it.
data class DashboardUiState(
    val isLoading: Boolean = false,
    val sports: List<Sport> = emptyList(),
    val entityTotal: Int = 0,
    val errorMessage: String? = null
)
