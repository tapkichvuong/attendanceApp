package com.atc.team10.attendancetracking.external.controller

import androidx.core.util.Pair
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.atc.team10.attendancetracking.data.model.request.LoginRequest
import com.atc.team10.attendancetracking.data.remote.ApiHelper
import com.atc.team10.attendancetracking.utils.AppExt.sendLog
import com.atc.team10.attendancetracking.utils.AppExt.updateValue
import com.atc.team10.attendancetracking.utils.AppUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginController : AbsController() {
    var isLoading = MutableLiveData<Boolean>()
    var notifyError = MutableLiveData("")
    var loginResult = MutableLiveData<Pair<String, String>>()

    fun login(loginRequest: LoginRequest) {
        isLoading.updateValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = ApiHelper.apiService.login(loginRequest)
                if (response.isSuccessful) {
                    val userResponse = response.body()
                    userResponse?.let { response ->
                        if (response.token != null) {
                            ApiHelper.authToken = response.token
                            ApiHelper.createRetrofit()
                            val decodeJwt = AppUtils.decodeJwtToken(response.token)
                            withContext(Dispatchers.Main) {
                                isLoading.updateValue(false)
                                notifyError.updateValue("")
                                loginResult.updateValue(decodeJwt)
                            }
                        } else {
                            val message = userResponse?.message ?: ""
                            withContext(Dispatchers.Main) {
                                notifyError.updateValue(message)
                            }
                        }
                    }
                } else {
                    val message = response.message()
                    withContext(Dispatchers.Main) {
                        notifyError.updateValue(message)
                        /** for test */
//                        isLoading.updateValue(false)
//                        notifyError.updateValue("")
//                        loginResult.updateValue(FakeDataSource.getStudentLoginInfo())
                    }
                }
            } catch (e: Exception) {
                sendLog("login message ${e.message}")
            } finally {
                withContext(Dispatchers.Main) {
                    isLoading.updateValue(false)
                }
            }
        }
    }
}