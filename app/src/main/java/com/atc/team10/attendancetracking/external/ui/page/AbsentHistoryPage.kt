package com.atc.team10.attendancetracking.external.ui.page

import android.view.View
import androidx.fragment.app.viewModels
import com.atc.team10.attendancetracking.R
import com.atc.team10.attendancetracking.databinding.PageAbsentHistoryBinding
import com.atc.team10.attendancetracking.external.controller.AbsentHistoryController

class AbsentHistoryPage : PageFragment() {
    override val controller by viewModels<AbsentHistoryController>()
    private lateinit var binding: PageAbsentHistoryBinding

    override fun getLayoutId() = R.layout.page_absent_history

    override fun initView(rootView: View, isRestore: Boolean) {
        binding = PageAbsentHistoryBinding.bind(rootView)
    }
}