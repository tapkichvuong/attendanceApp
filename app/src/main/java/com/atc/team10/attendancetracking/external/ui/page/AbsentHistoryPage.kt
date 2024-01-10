package com.atc.team10.attendancetracking.external.ui.page

import android.view.View
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.atc.team10.attendancetracking.R
import com.atc.team10.attendancetracking.databinding.PageAbsentHistoryBinding
import com.atc.team10.attendancetracking.external.controller.AbsentHistoryController
import com.atc.team10.attendancetracking.external.ui.adapter.ListAbsentAdapter
import com.atc.team10.attendancetracking.utils.AppConstant.BundleKey.LIST_HISTORY_ATTENDED
import com.atc.team10.attendancetracking.utils.AppConstant.BundleKey.SESSION_ID
import com.atc.team10.attendancetracking.utils.AppExt.getPreviousFragment
import com.atc.team10.attendancetracking.utils.AppExt.onClick
import com.atc.team10.attendancetracking.utils.AppExt.onClickSafely
import com.atc.team10.attendancetracking.utils.AppExt.setupOnBackPressedCallback

class AbsentHistoryPage : PageFragment() {
    override val controller by viewModels<AbsentHistoryController>()
    private lateinit var binding: PageAbsentHistoryBinding
    private lateinit var listAbsentAdapter: ListAbsentAdapter
    private lateinit var emptyView: View
    private lateinit var onBackPressedCallback: OnBackPressedCallback
    private var sessionId: Long = -1L
    private var listHistoryAttended: List<String> = emptyList()

    override fun getLayoutId() = R.layout.page_absent_history

    override fun initView(rootView: View, isRestore: Boolean) {
        binding = PageAbsentHistoryBinding.bind(rootView)
        sessionId = arguments?.getLong(SESSION_ID) ?: -1L
        listHistoryAttended = arguments?.getStringArrayList(LIST_HISTORY_ATTENDED) ?: emptyList()
        bindView()
        onBackPressedCallback = requireActivity().setupOnBackPressedCallback {
            val currentPage = requireActivity().getPreviousFragment()
            if (currentPage is ViewSessionDetailPage) {
                requireActivity().supportFragmentManager.popBackStack()
                currentPage.refresh()
            }
        }
        binding.ivBack.onClick {
            onBackPressedCallback.handleOnBackPressed()
        }
    }

    private fun bindView() {
        binding.root.onClickSafely {}
        emptyView = layoutInflater.inflate(R.layout.item_empty, null, false)
        emptyView.findViewById<TextView>(R.id.tvEmpty).text = "No history"
        listAbsentAdapter = ListAbsentAdapter().apply {
            setEmptyView(emptyView)
            setNewInstance(listHistoryAttended.toMutableList())
        }
        binding.rvAbsent.adapter = listAbsentAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        onBackPressedCallback.remove()
    }
}