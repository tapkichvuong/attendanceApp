package com.atc.team10.attendancetracking.external.ui.page

import android.view.View
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.atc.team10.attendancetracking.R
import com.atc.team10.attendancetracking.databinding.PageAbsentHistoryBinding
import com.atc.team10.attendancetracking.external.controller.AbsentHistoryController
import com.atc.team10.attendancetracking.external.ui.adapter.ListAbsentAdapter
import com.atc.team10.attendancetracking.utils.AppConstant
import com.atc.team10.attendancetracking.utils.AppExt.getPreviousFragment
import com.atc.team10.attendancetracking.utils.AppExt.gone
import com.atc.team10.attendancetracking.utils.AppExt.onClickSafely
import com.atc.team10.attendancetracking.utils.AppExt.setupOnBackPressedCallback
import com.atc.team10.attendancetracking.utils.AppExt.visible

class AbsentHistoryPage : PageFragment() {
    override val controller by viewModels<AbsentHistoryController>()
    private lateinit var binding: PageAbsentHistoryBinding
    private lateinit var listAbsentAdapter: ListAbsentAdapter
    private lateinit var emptyView: View

    override fun getLayoutId() = R.layout.page_absent_history

    override fun initView(rootView: View, isRestore: Boolean) {
        binding = PageAbsentHistoryBinding.bind(rootView)
        controller.sessionId = arguments?.getLong(AppConstant.BundleKey.SESSION_ID) ?: -1L
        bindView()
        initObserver()
        requireActivity().setupOnBackPressedCallback {
            val currentPage = requireActivity().getPreviousFragment()
            if (currentPage is ViewSessionDetailPage) {
                requireActivity().supportFragmentManager.popBackStack()
                currentPage.refresh()
            }
        }
        controller.loadHistoryAbsent()
    }

    private fun bindView() {
        binding.root.onClickSafely {}
        emptyView = layoutInflater.inflate(R.layout.item_empty, null, false)
        emptyView.findViewById<TextView>(R.id.tvEmpty).text = "No history"
        listAbsentAdapter = ListAbsentAdapter()
        binding.rvAbsent.adapter = listAbsentAdapter
        binding.swipeRefresh.setOnRefreshListener {
            controller.loadHistoryAbsent()
        }
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
        controller.absentResponse.observe(viewLifecycleOwner) {
            val listAbsentStudent = it.studentCode
            if (listAbsentStudent.isEmpty()) {
                listAbsentAdapter.setEmptyView(emptyView)
            } else {
                listAbsentAdapter.setNewInstance(listAbsentStudent.toMutableList())
            }
        }
    }
}