package com.atc.team10.attendancetracking.data.remote

import com.atc.team10.attendancetracking.data.model.request.JoinSessionRequest
import com.atc.team10.attendancetracking.data.model.request.LoginRequest
import com.atc.team10.attendancetracking.data.model.request.RegisterRequest
import com.atc.team10.attendancetracking.data.model.response.AbsentResponse
import com.atc.team10.attendancetracking.data.model.response.ActiveSessionResponse
import com.atc.team10.attendancetracking.data.model.response.LoginResponse
import com.atc.team10.attendancetracking.data.model.response.RegisterResponse
import com.atc.team10.attendancetracking.data.model.response.SessionResponse
import com.atc.team10.attendancetracking.data.model.response.TotalStudentResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("auth/register")
    suspend fun register(@Body body: RegisterRequest): Response<RegisterResponse>

    @POST("auth/authenticate")
    suspend fun login(@Body body: LoginRequest): Response<LoginResponse>

    @GET("student/sessions")
    suspend fun getStudentSessions(
        @Query("longitude") longitude: Double,
        @Query("latitude") latitude: Double
    ): Response<List<SessionResponse>>

    @POST("student/join-session")
    suspend fun joinSession(@Body body: JoinSessionRequest): Response<Boolean>

    @GET("teacher/sessions")
    suspend fun getTeacherSessions(): Response<List<SessionResponse>>

    @POST("teacher/activate")
    suspend fun activeSession(@Query("id") sessionId: Long): Response<ActiveSessionResponse>

    @GET("teacher/check-active-session")
    suspend fun isActiveSession(@Query("sessionId") sessionId: Long): Response<ActiveSessionResponse>

    @GET("teacher/absent-registered-students")
    suspend fun getHistoryOfAbsentStudents(@Query("sessionId") sessionId: Long): Response<AbsentResponse>

    @GET("teacher/total-students")
    suspend fun getTotalStudents(@Query("sessionId") sessionId: Long): Response<TotalStudentResponse>
}