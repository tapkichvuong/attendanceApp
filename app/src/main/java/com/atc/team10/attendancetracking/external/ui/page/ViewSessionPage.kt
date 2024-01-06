package com.atc.team10.attendancetracking.external.ui.page

import android.view.View
import androidx.fragment.app.viewModels
import com.atc.team10.attendancetracking.R
import com.atc.team10.attendancetracking.databinding.PageViewSessionBinding
import com.atc.team10.attendancetracking.external.controller.ViewSessionController
import com.atc.team10.attendancetracking.utils.AppConstant.BundleKey.USER_CODE
import com.atc.team10.attendancetracking.utils.AppConstant.BundleKey.USER_ROLE

class ViewSessionPage : PageFragment() {
    override val controller by viewModels<ViewSessionController>()
    private lateinit var binding: PageViewSessionBinding

    override fun getLayoutId() = R.layout.page_view_session

    override fun initView(rootView: View, isRestore: Boolean) {
        binding = PageViewSessionBinding.bind(rootView)
        controller.userCode = arguments?.getString(USER_CODE) ?: ""
        controller.userRole = arguments?.getString(USER_ROLE) ?: ""
        // get list session by role and update
    }
}