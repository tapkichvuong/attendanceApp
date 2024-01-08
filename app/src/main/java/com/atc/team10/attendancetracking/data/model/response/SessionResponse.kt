package com.atc.team10.attendancetracking.data.model.response

data class SessionResponse(
    val Id: Long,
    val timeStart: String,
    val timeEnd: String,
    val isActive: Boolean,
    val lessonName: String,
    val subjectName: String
)