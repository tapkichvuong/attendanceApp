package com.atc.team10.attendancetracking.data.remote

import com.atc.team10.attendancetracking.data.model.request.AbsentRequest
import com.atc.team10.attendancetracking.data.model.request.ActiveSessionRequest
import com.atc.team10.attendancetracking.data.model.request.LoginRequest
import com.atc.team10.attendancetracking.data.model.request.RegisterRequest
import com.atc.team10.attendancetracking.data.model.request.TotalStudentRequest
import com.atc.team10.attendancetracking.data.model.response.AbsentResponse
import com.atc.team10.attendancetracking.data.model.response.ActiveSessionResponse
import com.atc.team10.attendancetracking.data.model.response.LoginResponse
import com.atc.team10.attendancetracking.data.model.response.RegisterResponse
import com.atc.team10.attendancetracking.data.model.response.SessionResponse
import com.atc.team10.attendancetracking.data.model.response.TotalStudentResponse
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

    @GET("student/sessions")
    suspend fun getStudentSessions(): Response<List<SessionResponse>>

    @Multipart
    @POST("student/join-session")
    suspend fun joinSession(@Part("sessionId") sessionId: Long, @Part imageFile: MultipartBody.Part): Response<Boolean>

    @GET("teacher/sessions")
    suspend fun getTeacherSessions(): Response<List<SessionResponse>>

    @GET("teacher/activate")
    suspend fun activeSession(@Body body: ActiveSessionRequest): Response<ActiveSessionResponse>

    @GET("teacher/absent-registered-students")
    suspend fun getHistoryOfAbsentStudents(@Body body: AbsentRequest): Response<AbsentResponse>

    @GET("teacher/total-students")
    suspend fun getTotalStudents(@Body body: TotalStudentRequest): Response<TotalStudentResponse>
}