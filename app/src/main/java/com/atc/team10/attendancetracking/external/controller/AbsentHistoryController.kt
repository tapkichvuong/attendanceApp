package com.atc.team10.attendancetracking.external.controller

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.atc.team10.attendancetracking.data.model.request.AbsentRequest
import com.atc.team10.attendancetracking.data.model.response.AbsentResponse
import com.atc.team10.attendancetracking.data.remote.ApiHelper
import com.atc.team10.attendancetracking.utils.AppExt
import com.atc.team10.attendancetracking.utils.AppExt.updateValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AbsentHistoryController : AbsController() {
    var sessionId: Long = -1L
    var isLoading = MutableLiveData(false)
    var absentResponse = MutableLiveData<AbsentResponse>()

    fun loadHistoryAbsent() {
        isLoading.updateValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val requestBody = AbsentRequest(sessionId)
                val response = ApiHelper.apiService.getHistoryOfAbsentStudents(requestBody)
                if (response.isSuccessful) {
                    val listAbsentResponse = response.body()
                    AppExt.sendLog("loadHistoryAbsent - ${listAbsentResponse?.studentCode?.size}")
                    withContext(Dispatchers.Main) {
                        isLoading.updateValue(false)
                        absentResponse.updateValue(listAbsentResponse!!)
                    }
                } else {
                    val message = response.message()
                    withContext(Dispatchers.Main) {
                        isLoading.updateValue(false)
                        absentResponse.updateValue(AbsentResponse(emptyList()))
                    }
                }
            } catch (e: Exception) {
                AppExt.sendLog("get student session message ${e.message}")
            } finally {
                withContext(Dispatchers.Main) {
                    isLoading.updateValue(false)
                }
            }
        }
    }
}