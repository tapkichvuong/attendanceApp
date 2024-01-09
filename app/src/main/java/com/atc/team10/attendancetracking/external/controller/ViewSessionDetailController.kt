package com.atc.team10.attendancetracking.external.controller

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.atc.team10.attendancetracking.data.model.response.AbsentResponse
import com.atc.team10.attendancetracking.data.model.response.TotalStudentResponse
import com.atc.team10.attendancetracking.data.remote.ApiHelper
import com.atc.team10.attendancetracking.utils.AppExt
import com.atc.team10.attendancetracking.utils.AppExt.updateValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * TODO: Remember to check all of cases: Load fail, load success but include message (Login)
 */

class ViewSessionDetailController : AbsController() {
    var isActive = MutableLiveData<Boolean>()
    var totalStudent = MutableLiveData<TotalStudentResponse?>()
    var listAbsentStudent = MutableLiveData<AbsentResponse?>()

    fun getTotalStudent(sessionId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = ApiHelper.apiService.getTotalStudents(sessionId)
                if (response.isSuccessful) {
                    val studentsResponse = response.body()
                    AppExt.sendLog("getTotalStudent - ${studentsResponse?.countTotalStudent}")
                    withContext(Dispatchers.Main) {
                        totalStudent.updateValue(studentsResponse)
                    }
                } else {
                    val message = response.message()
                    withContext(Dispatchers.Main) {
                        totalStudent.updateValue(TotalStudentResponse(0, emptyList()))
                    }
                }
            } catch (e: Exception) {
                AppExt.sendLog("getTotalStudent message ${e.message}")
                withContext(Dispatchers.Main) {
                    totalStudent.updateValue(TotalStudentResponse(0, emptyList()))
                }
            }
        }
    }

    fun loadListAbsentStudent(sessionId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = ApiHelper.apiService.getHistoryOfAbsentStudents(sessionId)
                if (response.isSuccessful) {
                    val absentResponse = response.body()
                    AppExt.sendLog("loadListAbsentStudent - ${absentResponse?.studentCode?.size}")
                    withContext(Dispatchers.Main) {
                        listAbsentStudent.updateValue(absentResponse)
                    }
                } else {
                    val message = response.message()
                    withContext(Dispatchers.Main) {
                        listAbsentStudent.updateValue(AbsentResponse(emptyList()))
                    }
                }
            } catch (e: Exception) {
                AppExt.sendLog("loadListAbsentStudent message ${e.message}")
                listAbsentStudent.updateValue(AbsentResponse(emptyList()))
            }
        }
    }

    fun checkStatusOfSession(sessionId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = ApiHelper.apiService.isActiveSession(sessionId)
                if (response.isSuccessful) {
                    val activeResponse = response.body()
                    AppExt.sendLog("checkStatusOfSession - status = ${activeResponse?.status}, message = ${activeResponse?.message}")
                    withContext(Dispatchers.Main) {
                        isActive.updateValue(activeResponse?.status == 1L)
                    }
                } else {
                    val message = response.message()
                    withContext(Dispatchers.Main) {
                        isActive.updateValue(false)
                    }
                }
            } catch (e: Exception) {
                AppExt.sendLog("checkStatusOfSession message ${e.message}")
                isActive.updateValue(false)
            }
        }
    }

    fun activeSession(sessionId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = ApiHelper.apiService.activeSession(sessionId)
                if (response.isSuccessful) {
                    val activeResponse = response.body()
                    AppExt.sendLog("activeSession - status = ${activeResponse?.status}, message = ${activeResponse?.message}")
                    withContext(Dispatchers.Main) {
                        isActive.updateValue(activeResponse?.status == 1L)
                    }
                } else {
                    val message = response.message()
                    withContext(Dispatchers.Main) {
                        isActive.updateValue(false)
                    }
                }
            } catch (e: Exception) {
                AppExt.sendLog("activeSession message ${e.message}")
                isActive.updateValue(false)
            }
        }
    }
}