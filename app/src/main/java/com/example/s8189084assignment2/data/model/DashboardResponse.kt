package com.example.s8189084assignment2.data.model

// The response returned by the dashboard endpoint.
data class DashboardResponse(
    val entities: List<Sport>,
    val entityTotal: Int
)
