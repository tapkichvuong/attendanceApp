package com.atc.team10.attendancetracking.external.ui.page

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import com.atc.team10.attendancetracking.R
import com.atc.team10.attendancetracking.databinding.PageViewSessionBinding
import com.atc.team10.attendancetracking.external.controller.ViewSessionController
import com.atc.team10.attendancetracking.external.ui.adapter.ListSessionAdapter
import com.atc.team10.attendancetracking.external.ui.dialog.DialogQuestionBuilder
import com.atc.team10.attendancetracking.utils.AppConstant
import com.atc.team10.attendancetracking.utils.AppConstant.BundleKey.USER_CODE
import com.atc.team10.attendancetracking.utils.AppConstant.BundleKey.USER_ROLE
import com.atc.team10.attendancetracking.utils.AppExt.gone
import com.atc.team10.attendancetracking.utils.AppExt.isCameraPermisionGranted
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
        controller.userCode = arguments?.getString(USER_CODE) ?: ""
        controller.userRole = arguments?.getString(USER_ROLE) ?: ""
        bindView()
        initObserver()
        requireActivity().setupOnBackPressedCallback {
            requireActivity().finish()
        }
        controller.viewStudentSession()
    }

    private fun bindView() {
        binding.root.onClickSafely {}
        binding.tvUserCode.text = "Hi, ${controller.userCode}"
        emptyView = layoutInflater.inflate(R.layout.item_empty, null, false)
        emptyView.findViewById<TextView>(R.id.tvEmpty).text = "No active session"
        // get list session by role and update
        listSessionAdapter = ListSessionAdapter().apply {
            setOnItemClickListener { _, _, position ->
                handleClickSession(position)
            }
        }
        binding.rvCourse.adapter = listSessionAdapter
        binding.swipeRefresh.setOnRefreshListener {
            controller.viewStudentSession()
        }
    }

    override fun refresh() {
        controller.viewStudentSession()
    }

    private fun initObserver() {
        controller.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                binding.loadingView.visible()
            } else {
                binding.swipeRefresh.isRefreshing = false
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

    private fun handleClickSession(position: Int) {
        if (requireContext().isCameraPermisionGranted()) {
            val id = listSessionAdapter.getItem(position).Id
            val targetPage =
                if (controller.userRole == "TEACHER") ViewSessionDetailPage() else FaceDetectionPage()
            targetPage.apply {
                arguments = Bundle().apply {
                    putLong(AppConstant.BundleKey.SESSION_ID, id)
                }
            }
            PageUtils.addFragment(requireActivity(), targetPage, false)
        } else {
            // request camera permision
            requestCameraLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private val requestCameraLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { grant ->
            if (grant) {
                // do nothing
                // student must click join session again
            } else {
                val isGoToSetting = !ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.CAMERA
                )
                showRequestPermissionCameraDialog(isGoToSetting)
            }
        }

    private fun showRequestPermissionCameraDialog(isGoToSetting: Boolean) {
        val message = "You need to allow for camera permission before joining to session!"
        DialogQuestionBuilder(requireContext())
            .setMessage(message)
            .setPositiveButton("Open Setting") {
                if (isGoToSetting) {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    intent.data = Uri.parse("package:${requireContext().packageName}")
                    startActivity(intent)
                } else {
                    requestCameraLauncher.launch(Manifest.permission.CAMERA)
                }
            }.build()
            .show()
    }
}