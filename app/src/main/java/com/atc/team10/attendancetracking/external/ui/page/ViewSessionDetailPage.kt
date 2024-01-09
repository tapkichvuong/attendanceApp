package com.atc.team10.attendancetracking.external.ui.page

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.atc.team10.attendancetracking.R
import com.atc.team10.attendancetracking.databinding.PageViewSessionDetailBinding
import com.atc.team10.attendancetracking.external.controller.ViewSessionDetailController
import com.atc.team10.attendancetracking.utils.AppConstant.BundleKey.LESSON_NAME
import com.atc.team10.attendancetracking.utils.AppConstant.BundleKey.SESSION_ID
import com.atc.team10.attendancetracking.utils.AppConstant.BundleKey.SUBJECT_NAME
import com.atc.team10.attendancetracking.utils.AppExt.getPreviousFragment
import com.atc.team10.attendancetracking.utils.AppExt.onClick
import com.atc.team10.attendancetracking.utils.AppExt.onClickSafely
import com.atc.team10.attendancetracking.utils.AppExt.setupOnBackPressedCallback
import com.atc.team10.attendancetracking.utils.PageUtils

class ViewSessionDetailPage : PageFragment() {
    override val controller by viewModels<ViewSessionDetailController>()
    private lateinit var binding: PageViewSessionDetailBinding
    private lateinit var onBackPressedCallback: OnBackPressedCallback
    var sessionId: Long = -1L
    var lessonName: String = ""
    var subjectName: String = ""

    override fun getLayoutId() = R.layout.page_view_session_detail

    override fun initView(rootView: View, isRestore: Boolean) {
        binding = PageViewSessionDetailBinding.bind(rootView)
        sessionId = arguments?.getLong(SESSION_ID) ?: -1L
        lessonName = arguments?.getString(LESSON_NAME) ?: ""
        subjectName = arguments?.getString(SUBJECT_NAME) ?: ""
        bindView()
        onBackPressedCallback = requireActivity().setupOnBackPressedCallback {
            val currentPage = requireActivity().getPreviousFragment()
            if (currentPage is ViewSessionPage) {
                requireActivity().supportFragmentManager.popBackStack()
                currentPage.refresh()
            }
        }
        binding.ivBack.onClick {
            onBackPressedCallback.handleOnBackPressed()
        }
        controller.loadSessionOverviewInfo()
    }

    private fun bindView() {
        binding.root.onClickSafely {}
        binding.btnHistory.onClick {
            val absentHistoryPage = AbsentHistoryPage().apply {
                arguments = Bundle().apply {
                    putLong(SESSION_ID, sessionId)
                }
            }
            PageUtils.addFragment(requireActivity(), absentHistoryPage, false)
        }
        binding.btnAttendance.onClick {
            controller.activeSession()
        }
        binding.lessonName.text = lessonName
        binding.countStudentInfo.subTitle.text = "Student"
//        binding.countStudentInfo.mainTitle.text = "Student"
    }

    override fun onDestroy() {
        super.onDestroy()
        onBackPressedCallback.remove()
    }
}