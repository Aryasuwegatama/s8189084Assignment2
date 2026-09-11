package com.example.s8189084assignment2.data.remote

import com.example.s8189084assignment2.data.model.DashboardResponse
import com.example.s8189084assignment2.data.model.LoginRequest
import com.example.s8189084assignment2.data.model.LoginResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

// Retrofit builds the network calls from these annotations.
interface ApiService {

    // Logs in with a Student ID and password, and returns a keypass on success.
    @POST("br/auth")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    // Gets the dashboard for a given keypass.
    @GET("dashboard/{keypass}")
    suspend fun getDashboard(@Path("keypass") keypass: String): DashboardResponse
}
