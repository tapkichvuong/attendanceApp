package com.atc.team10.attendancetracking.external.ui.page

import android.os.Bundle
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
import com.atc.team10.attendancetracking.utils.AppExt.onClick
import com.atc.team10.attendancetracking.utils.AppExt.visible
import com.atc.team10.attendancetracking.utils.PageUtils

class LoginPage: PageFragment() {
    override val controller by viewModels<LoginController>()
    private lateinit var binding: PageLoginBinding
    private var loadingDialog: LoadingDialog? = null

    override fun getLayoutId(): Int = R.layout.page_login

    override fun initView(rootView: View, isRestore: Boolean) {
        binding = PageLoginBinding.bind(rootView)
        val scaleAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.scale_anim)
        binding.imgAppIcon.startAnimation(scaleAnimation)
        binding.btnLogin.onClick {
                val userCode = binding.edtUserCode.text.toString()
                val password = binding.edtPassword.text.toString()
                val loginRequest = LoginRequest(userCode, password)
                controller.login(loginRequest)
        }
        observeLogin()
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