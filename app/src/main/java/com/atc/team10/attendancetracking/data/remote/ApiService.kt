package com.atc.team10.attendancetracking.data.remote

import com.atc.team10.attendancetracking.data.model.request.LoginRequest
import com.atc.team10.attendancetracking.data.model.request.RegisterRequest
import com.atc.team10.attendancetracking.data.model.response.LoginResponse
import com.atc.team10.attendancetracking.data.model.response.RegisterResponse
import com.atc.team10.attendancetracking.data.model.response.SessionResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @POST("auth/register")
    suspend fun register(@Body body: RegisterRequest): Response<RegisterResponse>

    @POST("auth/authenticate")
    suspend fun login(@Body body: LoginRequest): Response<LoginResponse>

    @GET("api/v1/student/sessions")
    suspend fun getStudentSessions(): Response<List<SessionResponse>>

    @Multipart
    @POST("api/v1/student")
    suspend fun joinSession(@Part("sessionId") sessionId: Long, @Part imageFile: MultipartBody.Part): Response<Boolean>

    suspend fun getTeacherSessions(): Response<List<SessionResponse>>
}