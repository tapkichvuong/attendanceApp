package com.atc.team10.attendancetracking.external.ui.page

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.util.Pair
import androidx.fragment.app.viewModels
import com.atc.team10.attendancetracking.R
import com.atc.team10.attendancetracking.data.model.request.LoginRequest
import com.atc.team10.attendancetracking.databinding.PageLoginBinding
import com.atc.team10.attendancetracking.external.controller.LoginController
import com.atc.team10.attendancetracking.external.ui.dialog.LoadingDialog
import com.atc.team10.attendancetracking.utils.AppConstant.BundleKey.USER_CODE
import com.atc.team10.attendancetracking.utils.AppConstant.BundleKey.USER_ROLE
import com.atc.team10.attendancetracking.utils.AppExt.invisible
import com.atc.team10.attendancetracking.utils.AppExt.isConnectionAvailable
import com.atc.team10.attendancetracking.utils.AppExt.onClick
import com.atc.team10.attendancetracking.utils.AppExt.onClickSafely
import com.atc.team10.attendancetracking.utils.AppExt.visible
import com.atc.team10.attendancetracking.utils.PageUtils
import com.atc.team10.attendancetracking.utils.PrefUtils

class LoginPage: PageFragment() {
    override val controller by viewModels<LoginController>()
    private lateinit var binding: PageLoginBinding
    private var loadingDialog: LoadingDialog? = null

    override fun getLayoutId(): Int = R.layout.page_login

    override fun initView(rootView: View, isRestore: Boolean) {
        binding = PageLoginBinding.bind(rootView)
        bindView()
        observeLogin()
    }

    private fun bindView() {
        val scaleAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.scale_anim)
        binding.imgAppIcon.startAnimation(scaleAnimation)
        binding.root.onClickSafely {}
        binding.ivTogglePassword.onClick {
            if (binding.edtPassword.transformationMethod is PasswordTransformationMethod) {
                binding.edtPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                binding.ivTogglePassword.setImageResource(R.drawable.ic_show_password)
            } else {
                binding.edtPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.ivTogglePassword.setImageResource(R.drawable.ic_hide_password)
            }
        }
        val isRememberPassword = PrefUtils.rememberPassword
        binding.cbRememberPassword.isChecked = isRememberPassword
        if (isRememberPassword) {
            binding.edtUserCode.setText(PrefUtils.userCode)
            binding.edtPassword.setText(PrefUtils.userPassword)
        }
        binding.cbRememberPassword.setOnCheckedChangeListener { _, isChecked ->
            PrefUtils.rememberPassword = isChecked
        }
        binding.btnLogin.onClick {
            val userCode = binding.edtUserCode.text.toString()
            val password = binding.edtPassword.text.toString()
            val loginRequest = LoginRequest(userCode, password)
            if (binding.cbRememberPassword.isChecked && userCode.isNotBlank() && password.isNotBlank()) {
                PrefUtils.rememberPassword = true
                PrefUtils.userCode = userCode
                PrefUtils.userPassword = password
            }
            if (requireContext().isConnectionAvailable()) {
                controller.login(loginRequest)
            }
        }
    }

    private fun observeLogin() {
        controller.isLoading.observe(viewLifecycleOwner) {
            showDialogLoading(it)
        }
        controller.notifyError.observe(viewLifecycleOwner) {
            showLoginError(it)
        }
        controller.loginResult.observe(viewLifecycleOwner) {
            executeLoginSuccess(it)
        }
    }

    private fun executeLoginSuccess(info: Pair<String, String>) {
        val viewSessionPage = ViewSessionPage()
        viewSessionPage.arguments = Bundle().apply {
            putString(USER_CODE, info.first)
            putString(USER_ROLE, info.second)
        }
        PageUtils.addFragment(requireActivity(), viewSessionPage, true)
    }

    private fun showDialogLoading(isShow: Boolean) {
        if (loadingDialog == null) {
            loadingDialog = LoadingDialog(requireContext())
        }
        if (isShow) {
            loadingDialog!!.setTextLoading("Logging in...")
            if (!loadingDialog!!.isShowing) loadingDialog!!.show()
        } else {
            if (loadingDialog!!.isShowing) loadingDialog!!.dismiss()
        }
    }

    private fun showLoginError(error: String) {
        if (error.isEmpty()) {
            binding.tvError.invisible()
        } else {
            binding.tvError.text = error
            binding.tvError.visible()
        }
    }
}