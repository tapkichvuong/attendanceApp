package com.atc.team10.attendancetracking.external.controller

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.atc.team10.attendancetracking.data.model.response.SessionResponse
import com.atc.team10.attendancetracking.data.remote.ApiHelper
import com.atc.team10.attendancetracking.utils.AppExt.sendLog
import com.atc.team10.attendancetracking.utils.AppExt.updateValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewSessionController : AbsController() {
    var isLoading = MutableLiveData(false)
    var listSession = MutableLiveData<List<SessionResponse>>()

    fun viewStudentSession(userRole: String, latitude: Double = 0.0, longitude: Double = 0.0) {
        isLoading.updateValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                sendLog("viewStudentSession] role = $userRole")
                sendLog("viewStudentSession] location: lat = $latitude, lng = $longitude")
                val response =
                    if (userRole == "STUDENT") ApiHelper.apiService.getStudentSessions(
                        longitude,
                        latitude
                    ) else ApiHelper.apiService.getTeacherSessions()
                if (response.isSuccessful) {
                    val listSessionResponse = response.body()
                    sendLog("viewStudentSession - ${listSessionResponse?.size}")
                    withContext(Dispatchers.Main) {
                        isLoading.updateValue(false)
                        listSession.updateValue(listSessionResponse!!)
                    }
                } else {
                    val message = response.message()
                    withContext(Dispatchers.Main) {
                        isLoading.updateValue(false)
                        listSession.updateValue(emptyList())
                    }
                    sendLog("get student session message1 ${message}")
                }
            } catch (e: Exception) {
                sendLog("get student session message2 ${e.message}")
                withContext(Dispatchers.Main) {
                    isLoading.updateValue(false)
                    listSession.updateValue(emptyList())
                }
            } finally {
                withContext(Dispatchers.Main) {
                    isLoading.updateValue(false)
                }
            }
        }
    }
}