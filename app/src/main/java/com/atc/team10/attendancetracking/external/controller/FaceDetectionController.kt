package com.atc.team10.attendancetracking.external.controller

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.atc.team10.attendancetracking.data.remote.ApiHelper
import com.atc.team10.attendancetracking.utils.AppExt
import com.atc.team10.attendancetracking.utils.AppExt.updateValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class FaceDetectionController : AbsController() {
    var sessionId: Long = -1L
    var imageFile: MutableLiveData<File?> = MutableLiveData(null)
    var isLoading = MutableLiveData(false)
    var isJoined = MutableLiveData(false)

    fun joinSession() {
        isLoading.updateValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), imageFile.value!!)
                val imageBody = MultipartBody.Part.createFormData("image", imageFile.value!!.name, requestFile)
                val response = ApiHelper.apiService.joinSession(sessionId, imageBody)
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
            } finally {
                withContext(Dispatchers.Main) {
                    isLoading.updateValue(false)
                }
            }
        }
    }
}