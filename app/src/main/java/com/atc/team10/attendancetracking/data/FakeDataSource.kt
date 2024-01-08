package com.atc.team10.attendancetracking.data

import com.atc.team10.attendancetracking.data.model.response.SessionResponse

object FakeDataSource {
    fun getListSession(): List<SessionResponse> {
        val list = ArrayList<SessionResponse>()
        for (index in 1L..10L) {
//            list.add(SessionResponse(index, LocalDateTime.now(), LocalDateTime.now(), true))
        }
        return list
    }
}