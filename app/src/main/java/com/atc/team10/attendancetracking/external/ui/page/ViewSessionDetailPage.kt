package com.atc.team10.attendancetracking.external.ui.page

import android.view.View
import androidx.fragment.app.viewModels
import com.atc.team10.attendancetracking.databinding.PageViewSessionDetailBinding
import com.atc.team10.attendancetracking.external.controller.ViewSessionDetailController

class ViewSessionDetailPage : PageFragment() {
    override val controller by viewModels<ViewSessionDetailController>()
    private lateinit var binding: PageViewSessionDetailBinding

    override fun getLayoutId(): Int {
        TODO("Not yet implemented")
    }

    override fun initView(rootView: View, isRestore: Boolean) {
        binding = PageViewSessionDetailBinding.bind(rootView)
    }
}