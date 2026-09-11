package com.example.s8189084assignment2.data.repository

import com.example.s8189084assignment2.data.model.DashboardResponse
import com.example.s8189084assignment2.data.model.LoginRequest
import com.example.s8189084assignment2.data.model.LoginResponse
import com.example.s8189084assignment2.data.remote.ApiService
import javax.inject.Inject

// Implements SportsRepository using ApiService.
class SportsRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : SportsRepository {

    override suspend fun login(username: String, password: String): LoginResponse {
        return apiService.login(LoginRequest(username, password))
    }

    override suspend fun getDashboard(keypass: String): DashboardResponse {
        return apiService.getDashboard(keypass)
    }
}
