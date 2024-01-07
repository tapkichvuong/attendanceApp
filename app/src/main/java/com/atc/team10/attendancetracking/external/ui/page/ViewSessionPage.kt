package com.atc.team10.attendancetracking.external.ui.page

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.atc.team10.attendancetracking.R
import com.atc.team10.attendancetracking.databinding.PageViewSessionBinding
import com.atc.team10.attendancetracking.external.controller.ViewSessionController
import com.atc.team10.attendancetracking.external.ui.adapter.ListSessionAdapter
import com.atc.team10.attendancetracking.utils.AppConstant.BundleKey.SESSION_ID
import com.atc.team10.attendancetracking.utils.AppConstant.BundleKey.USER_CODE
import com.atc.team10.attendancetracking.utils.AppConstant.BundleKey.USER_ROLE
import com.atc.team10.attendancetracking.utils.AppExt.gone
import com.atc.team10.attendancetracking.utils.AppExt.onClickSafely
import com.atc.team10.attendancetracking.utils.AppExt.setupOnBackPressedCallback
import com.atc.team10.attendancetracking.utils.AppExt.visible
import com.atc.team10.attendancetracking.utils.PageUtils

class ViewSessionPage : PageFragment() {
    override val controller by viewModels<ViewSessionController>()
    private lateinit var binding: PageViewSessionBinding
    private lateinit var listSessionAdapter: ListSessionAdapter
    private lateinit var emptyView: View

    override fun getLayoutId() = R.layout.page_view_session

    override fun initView(rootView: View, isRestore: Boolean) {
        binding = PageViewSessionBinding.bind(rootView)
        binding.root.onClickSafely {}
        controller.userCode = arguments?.getString(USER_CODE) ?: ""
        controller.userRole = arguments?.getString(USER_ROLE) ?: ""
        binding.tvUserCode.text = "Hi, ${controller.userCode}"
        emptyView = layoutInflater.inflate(R.layout.item_empty, null, false)
        emptyView.findViewById<TextView>(R.id.tvEmpty).text = "No active session"
        // get list session by role and update
        listSessionAdapter = ListSessionAdapter().apply {
            setOnItemClickListener { _, _, position ->
                val id = getItem(position).id
                val targetPage = if (controller.userRole == "TEACHER") ViewSessionDetailPage() else FaceDetectionPage()
                targetPage.apply {
                    arguments = Bundle().apply {
                        putLong(SESSION_ID, id)
                    }
                }
                PageUtils.addFragment(requireActivity(), targetPage, false)
            }
        }
        binding.rvCourse.adapter = listSessionAdapter
        initObserver()
        requireActivity().setupOnBackPressedCallback {
            requireActivity().finish()
        }
        controller.viewStudentSession()
    }

    override fun refresh() {
        controller.viewStudentSession()
    }

    private fun initObserver() {
        controller.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                binding.loadingView.visible()
            } else {
                binding.loadingView.gone()
            }
        }
        controller.listSession.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                listSessionAdapter.setEmptyView(emptyView)
            } else {
                listSessionAdapter.setNewInstance(it.toMutableList())
            }
        }
    }
}