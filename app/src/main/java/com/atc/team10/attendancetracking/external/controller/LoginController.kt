package com.atc.team10.attendancetracking.external.controller

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.atc.team10.attendancetracking.data.model.request.LoginRequest
import com.atc.team10.attendancetracking.data.remote.ApiHelper
import com.atc.team10.attendancetracking.utils.AppExt.updateValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginController : AbsController() {
    var isLoading = MutableLiveData(false)

    fun login(loginRequest: LoginRequest) {
        isLoading.updateValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = ApiHelper.apiService.login(loginRequest)
                if (response.isSuccessful) {
                    val userResponse = response.body()
                } else {
                    val message = response.message()
                }
            } catch (e: Exception) {

            } finally {
                withContext(Dispatchers.Main) {
                    isLoading.updateValue(false)

                }
            }
        }
    }
}