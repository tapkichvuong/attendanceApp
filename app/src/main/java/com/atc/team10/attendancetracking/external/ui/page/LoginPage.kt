package com.atc.team10.attendancetracking.external.ui.page

import android.Manifest
import android.animation.ObjectAnimator
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.util.Pair
import androidx.fragment.app.viewModels
import com.atc.team10.attendancetracking.R
import com.atc.team10.attendancetracking.data.model.request.LoginRequest
import com.atc.team10.attendancetracking.databinding.PageLoginBinding
import com.atc.team10.attendancetracking.external.controller.LoginController
import com.atc.team10.attendancetracking.external.ui.dialog.DialogQuestionBuilder
import com.atc.team10.attendancetracking.utils.AppConstant
import com.atc.team10.attendancetracking.utils.AppConstant.BundleKey.USER_CODE
import com.atc.team10.attendancetracking.utils.AppConstant.BundleKey.USER_ROLE
import com.atc.team10.attendancetracking.utils.AppExt.invisible
import com.atc.team10.attendancetracking.utils.AppExt.isConnectionAvailable
import com.atc.team10.attendancetracking.utils.AppExt.isLocationPermissionGranted
import com.atc.team10.attendancetracking.utils.AppExt.onClick
import com.atc.team10.attendancetracking.utils.AppExt.onClickSafely
import com.atc.team10.attendancetracking.utils.AppExt.setupOnBackPressedCallback
import com.atc.team10.attendancetracking.utils.AppExt.showShortToast
import com.atc.team10.attendancetracking.utils.AppExt.visible
import com.atc.team10.attendancetracking.utils.PageUtils
import com.atc.team10.attendancetracking.utils.PrefUtils

class LoginPage: PageFragment() {
    override val controller by viewModels<LoginController>()
    private lateinit var binding: PageLoginBinding
    private lateinit var onBackPressedCallback: OnBackPressedCallback

    override fun getLayoutId(): Int = R.layout.page_login

    override fun initView(rootView: View, isRestore: Boolean) {
        binding = PageLoginBinding.bind(rootView)
        bindView()
        onBackPressedCallback = requireActivity().setupOnBackPressedCallback {
            if (System.currentTimeMillis() - backPressedTime < AppConstant.DOUBLE_BACK_PRESSED_INTERVAL) {
                // If the time difference between two back presses is less than the interval, exit the app
                requireActivity().finish()
            } else {
                requireContext().showShortToast("Press back again to exit")
                backPressedTime = System.currentTimeMillis()
            }
        }
        if (!requireContext().isLocationPermissionGranted()) {
            requestLocationLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        observeLogin()
    }

    private val requestLocationLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { grant ->
        if (grant) {
            // do nothing
        } else {
            val isGoToSetting = !ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            showRequestPermissionLocationDialog(isGoToSetting)
        }
    }

    private fun showRequestPermissionLocationDialog(isGoToSetting: Boolean) {
        val message = "You need to allow for location permission before using this app!"
        DialogQuestionBuilder(requireContext())
            .setMessage(message)
            .setPositiveButton("Open Setting") {
                if (isGoToSetting) {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    intent.data = Uri.parse("package:${requireContext().packageName}")
                    startActivity(intent)
                } else {
                    requestLocationLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }.setNegativeButton("Close") {
                requireActivity().finish()
            }.build()
            .show()
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
            showDialogLoading(it, "Logging in...")
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

    private fun showLoginError(error: String) {
        if (error.isEmpty()) {
            binding.tvError.invisible()
        } else {
            binding.tvError.text = error
            binding.tvError.visible()
            val shakeAnimation = ObjectAnimator.ofFloat(binding.tvError, "translationX", 0f, 10f, -10f, 10f, -10f, 5f, -5f, 0f)
            shakeAnimation.duration = 700 // Duration of the animation in milliseconds
            shakeAnimation.interpolator = AccelerateDecelerateInterpolator() // Optional: Use a different interpolator for a different effect
            shakeAnimation.repeatCount = 0 // Repeat the animation infinitely
            shakeAnimation.start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        onBackPressedCallback.remove()
    }
}