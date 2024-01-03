package com.atc.team10.attendancetracking.data.model.request

data class RegisterRequest(
    val userCode: String,
    val password: String,
    val confirmPassword: String,
    val role: Boolean
)
