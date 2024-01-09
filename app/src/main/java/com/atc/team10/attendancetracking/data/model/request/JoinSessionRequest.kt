package com.atc.team10.attendancetracking.data.model.request

data class JoinSessionRequest(
    val sessionId: Long,
    val studentImageUrl: String
)
