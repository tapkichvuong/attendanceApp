package com.atc.team10.attendancetracking.external.page

import android.view.View
import androidx.fragment.app.viewModels
import com.atc.team10.attendancetracking.R
import com.atc.team10.attendancetracking.data.model.request.LoginRequest
import com.atc.team10.attendancetracking.databinding.LoginPageBinding
import com.atc.team10.attendancetracking.external.controller.LoginController
import com.atc.team10.attendancetracking.utils.UiUtils

class LoginPage: PageFragment() {
    override val controller by viewModels<LoginController>()
    private lateinit var binding: LoginPageBinding

    override fun getLayoutId(): Int = R.layout.login_page

    override fun initView(rootView: View, isRestore: Boolean) {
        binding = LoginPageBinding.bind(rootView)
        binding.btnLogin.setOnClickListener {
            if (UiUtils.isValidClick(it.id)) {
                val userCode = binding.edtUserCode.text.toString()
                val password = binding.edtPassword.text.toString()
                val loginRequest = LoginRequest(userCode, password)
                controller.login(loginRequest)
            }
        }
        observeLogin()
    }

    private fun observeLogin() {
        controller.isLoading.observe(viewLifecycleOwner) {

        }
    }
}