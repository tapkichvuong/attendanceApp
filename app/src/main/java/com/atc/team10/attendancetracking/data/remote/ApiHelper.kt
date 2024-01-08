package com.atc.team10.attendancetracking.data.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiHelper {
    var authToken = ""
    private const val TIME_OUT = 10L
    private const val BASE_URL =
        "https://9e15-2401-d800-9c07-1e57-88c4-ca38-94f3-ed0.ngrok-free.app/api/v1/"

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