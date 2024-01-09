package com.atc.team10.attendancetracking.data.model.response

data class SessionResponse(
    val id: Long,
    val timeStart: String,
    val timeEnd: String,
    val isActive: Boolean,
    val lessonName: String,
    val subjectName: String
)