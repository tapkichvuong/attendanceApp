package com.atc.team10.attendancetracking.data.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiHelper {
    private const val TIME_OUT = 10L
    private const val BASE_URL =
        "https://756a-2405-4802-1d14-2090-2036-bd70-c433-c446.ngrok-free.app/api/v1/"

    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(TIME_OUT, TimeUnit.SECONDS) // Connect timeout
        .readTimeout(TIME_OUT, TimeUnit.SECONDS)    // Read timeout
        .writeTimeout(TIME_OUT, TimeUnit.SECONDS)   // Write timeout
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    val apiService: ApiService
        get() = retrofit.create(ApiService::class.java)
}