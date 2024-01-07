package com.atc.team10.attendancetracking.data.model.response

import java.time.LocalDateTime

data class SessionResponse(
    val id: Long,
//    val lessonName: String,
//    val subjectName: String,
    val timeStart: LocalDateTime,
    val timeEnd: LocalDateTime,
    val isActive: Boolean
)