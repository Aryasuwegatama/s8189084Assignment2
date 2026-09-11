package com.example.s8189084assignment2.data.repository

import com.example.s8189084assignment2.data.model.DashboardResponse
import com.example.s8189084assignment2.data.model.LoginResponse

// The data operations available to a ViewModel.
interface SportsRepository {

    suspend fun login(username: String, password: String): LoginResponse

    suspend fun getDashboard(keypass: String): DashboardResponse
}
