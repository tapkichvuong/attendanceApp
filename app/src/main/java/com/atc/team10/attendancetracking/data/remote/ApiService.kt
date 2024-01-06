package com.atc.team10.attendancetracking.data.remote

import com.atc.team10.attendancetracking.data.model.request.LoginRequest
import com.atc.team10.attendancetracking.data.model.request.RegisterRequest
import com.atc.team10.attendancetracking.data.model.response.LoginResponse
import com.atc.team10.attendancetracking.data.model.response.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("api/user/register")
    suspend fun register(@Body body: RegisterRequest): Response<RegisterResponse>

    @POST("api/user/login")
    suspend fun login(@Body body: LoginRequest): Response<LoginResponse>
}