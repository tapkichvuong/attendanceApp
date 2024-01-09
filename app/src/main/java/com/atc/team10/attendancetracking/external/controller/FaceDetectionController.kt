package com.atc.team10.attendancetracking.external.controller

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.atc.team10.attendancetracking.data.model.request.JoinSessionRequest
import com.atc.team10.attendancetracking.data.remote.ApiHelper
import com.atc.team10.attendancetracking.utils.AppExt
import com.atc.team10.attendancetracking.utils.AppExt.updateValue
import com.atc.team10.attendancetracking.utils.AppUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class FaceDetectionController : AbsController() {
    var sessionId: Long = -1L
    var imageFile: File? = null
    var isLoading = MutableLiveData(false)
    var isJoined = MutableLiveData(false)
    var isFirstObserver = true

    fun joinSession() {
        isLoading.updateValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val requestBody = JoinSessionRequest(sessionId, AppUtils.imageToBase64(imageFile?.path))
                val response = ApiHelper.apiService.joinSession(requestBody)
                if (response.isSuccessful) {
                    val hasJoined = response.body()
                    withContext(Dispatchers.Main) {
                        isLoading.updateValue(false)
                        isJoined.updateValue(hasJoined)
                    }
                } else {
                    val message = response.message()
                    withContext(Dispatchers.Main) {
                        isLoading.updateValue(false)
                        isJoined.updateValue(false)
                    }
                }
            } catch (e: Exception) {
                AppExt.sendLog("join session message ${e.message}")
                isJoined.updateValue(false)
            } finally {
                withContext(Dispatchers.Main) {
                    isLoading.updateValue(false)
                }
            }
        }
    }
}