package com.atc.team10.attendancetracking.data.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiHelper {
    var authToken = ""
    private const val TIME_OUT = 30L // set time out 30s
    private const val BASE_URL =
        "https://de90-2401-d800-beb9-3b7b-b025-22de-9add-9d38.ngrok-free.app/api/v1/"

    private var retrofit: Retrofit = createRetrofit()

    fun createRetrofit(): Retrofit {
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(createHttpClient(authToken))
            .build()
        return retrofit
    }

    private fun createHttpClient(authToken: String): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(TIME_OUT, TimeUnit.SECONDS) // Connect timeout
            .readTimeout(TIME_OUT, TimeUnit.SECONDS)    // Read timeout
            .writeTimeout(TIME_OUT, TimeUnit.SECONDS)   // Write timeout
            .addInterceptor(AuthInterceptor(authToken))
            .build()
    }

    val apiService: ApiService
        get() = retrofit.create(ApiService::class.java)
}