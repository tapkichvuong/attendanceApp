package com.atc.team10.attendancetracking.data

import androidx.core.util.Pair
import com.atc.team10.attendancetracking.data.model.response.AbsentResponse
import com.atc.team10.attendancetracking.data.model.response.SessionResponse
import com.atc.team10.attendancetracking.data.model.response.TotalStudentResponse
import java.time.LocalDateTime

object FakeDataSource {
    fun getStudentLoginInfo(): Pair<String, String> {
        return Pair("123", "STUDENT")
    }

    fun getTeacherLoginInfo(): Pair<String, String> {
        return Pair("456", "TEACHER")
    }

    fun getListSession(): List<SessionResponse> {
        val list = ArrayList<SessionResponse>()
        for (index in 1L..10L) {
            list.add(SessionResponse(index, LocalDateTime.now().toString(), LocalDateTime.now().toString(), true, "Lesson $index", "Subject $index"))
        }
        return list
    }

    fun getDetectionStatus(): Boolean {
        return true
    }

    fun getListHistoryAbsent(): AbsentResponse {
        val listStudentCode = ArrayList<String>()
        for (index in 123..150) {
            listStudentCode.add(index.toString())
        }
        return AbsentResponse(listStudentCode)
    }

    fun getTotalCountStudent(): TotalStudentResponse {
        val listStudentCode = ArrayList<String>()
        for (index in 123..150) {
            listStudentCode.add(index.toString())
        }
        return TotalStudentResponse(listStudentCode.size, listStudentCode)
    }
}