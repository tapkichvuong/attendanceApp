package com.atc.team10.attendancetracking.external.ui.page

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.atc.team10.attendancetracking.R
import com.atc.team10.attendancetracking.databinding.PageViewSessionDetailBinding
import com.atc.team10.attendancetracking.external.controller.ViewSessionDetailController
import com.atc.team10.attendancetracking.external.ui.adapter.ListAbsentAdapter
import com.atc.team10.attendancetracking.utils.AppConstant.BundleKey.LESSON_NAME
import com.atc.team10.attendancetracking.utils.AppConstant.BundleKey.LIST_HISTORY_ATTENDED
import com.atc.team10.attendancetracking.utils.AppConstant.BundleKey.SESSION_ID
import com.atc.team10.attendancetracking.utils.AppConstant.BundleKey.SUBJECT_NAME
import com.atc.team10.attendancetracking.utils.AppExt.getPreviousFragment
import com.atc.team10.attendancetracking.utils.AppExt.onClick
import com.atc.team10.attendancetracking.utils.AppExt.onClickSafely
import com.atc.team10.attendancetracking.utils.AppExt.setupOnBackPressedCallback
import com.atc.team10.attendancetracking.utils.PageUtils
import kotlinx.coroutines.Job

class ViewSessionDetailPage : PageFragment() {
    override val controller by viewModels<ViewSessionDetailController>()
    private lateinit var binding: PageViewSessionDetailBinding
    private lateinit var onBackPressedCallback: OnBackPressedCallback
    private lateinit var listAbsentAdapter: ListAbsentAdapter
    private lateinit var emptyView: View
    var sessionId: Long = -1L
    var lessonName: String = ""
    var subjectName: String = ""
    private var loadAbsentStudentJob: Job? = null

    override fun getLayoutId() = R.layout.page_view_session_detail

    override fun initView(rootView: View, isRestore: Boolean) {
        binding = PageViewSessionDetailBinding.bind(rootView)
        sessionId = arguments?.getLong(SESSION_ID) ?: -1L
        lessonName = arguments?.getString(LESSON_NAME) ?: ""
        subjectName = arguments?.getString(SUBJECT_NAME) ?: ""
        bindView()
        initObserver()
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
        controller.getTotalStudent(sessionId)
        controller.checkStatusOfSession(sessionId)
    }

    private fun bindView() {
        binding.root.onClickSafely {}
        binding.btnHistory.onClick {
            val args = Bundle().apply {
                putLong(SESSION_ID, sessionId)

                // get list student checked in
                val attendedStudentList = controller.listTotalStudent.minus((controller.listAbsentStudent.value?.studentCode ?: emptyList()).toSet())
                putStringArrayList(LIST_HISTORY_ATTENDED, ArrayList(attendedStudentList))
            }
            val absentHistoryPage = AbsentHistoryPage().apply {
                arguments = args
            }
            PageUtils.addFragment(requireActivity(), absentHistoryPage, false)
        }
        binding.btnAttendance.onClick {
            controller.activeSession(sessionId)
        }
        binding.lessonName.text = lessonName
        binding.countStudentInfo.subTitle.text = "Student"
        emptyView = layoutInflater.inflate(R.layout.item_empty, null, false)
        emptyView.findViewById<TextView>(R.id.tvEmpty).text = "No students are absent"
        listAbsentAdapter = ListAbsentAdapter()
        binding.rvAbsent.adapter = listAbsentAdapter
    }

    private fun initObserver() {
        controller.isOnlyCheckStatus.observe(viewLifecycleOwner) {
            if (it) {
                setButtonCheckingStatus()
                loadAbsentStudentJob?.cancel()
                loadAbsentStudentJob = controller.loadListAbsentStudent(sessionId, it)
            } else {
                setButtonIdleStatus()
                loadAbsentStudentJob?.cancel()
            }
        }
        controller.isActive.observe(viewLifecycleOwner) {
            if (it) {
                setButtonCheckingStatus()
                loadAbsentStudentJob?.cancel()
                loadAbsentStudentJob = controller.loadListAbsentStudent(sessionId, it)
            } else {
                setButtonIdleStatus()
                loadAbsentStudentJob?.cancel()
            }
        }
        controller.totalStudent.observe(viewLifecycleOwner) {
            it?.let {
                binding.countStudentInfo.mainTitle.text = it.countTotalStudent.toString()
            }
        }
        controller.listAbsentStudent.observe(viewLifecycleOwner) {
            it?.let {
                val listAbsentStudent = it.studentCode
                if (listAbsentStudent.isEmpty()) {
                    listAbsentAdapter.setEmptyView(emptyView)
                } else {
                    listAbsentAdapter.setNewInstance(listAbsentStudent.toMutableList())
                }
            }
        }
    }

    private fun setButtonCheckingStatus() {
        with(binding.btnAttendance) {
            background = AppCompatResources.getDrawable(requireContext(), R.drawable.bg_button_attendance_stop)
            text = "Stop"
            setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        }
    }

    private fun setButtonIdleStatus() {
        with(binding.btnAttendance) {
            background = AppCompatResources.getDrawable(requireContext(), R.drawable.bg_button_attendance_checking)
            text = "Attendance"
            setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        onBackPressedCallback.remove()
        loadAbsentStudentJob?.cancel()
    }
}