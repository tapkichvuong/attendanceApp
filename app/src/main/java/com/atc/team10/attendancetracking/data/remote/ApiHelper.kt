package com.atc.team10.attendancetracking.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiHelper {
    private const val BASE_URL = "https://cc0a-2401-d800-7301-dffa-8807-7837-12b5-8b35.ngrok-free.app/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ApiService
        get() = retrofit.create(ApiService::class.java)
}