package com.atc.team10.attendancetracking.data.model.response

data class UserResponse(
    val userCode: String,
    val token: String,
    val role: Boolean
)
