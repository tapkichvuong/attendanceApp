package com.atc.team10.attendancetracking.external.ui.page

import android.view.View
import androidx.fragment.app.viewModels
import com.atc.team10.attendancetracking.R
import com.atc.team10.attendancetracking.databinding.PageViewSessionDetailBinding
import com.atc.team10.attendancetracking.external.controller.ViewSessionDetailController
import com.atc.team10.attendancetracking.utils.AppExt.getPreviousFragment
import com.atc.team10.attendancetracking.utils.AppExt.onClickSafely
import com.atc.team10.attendancetracking.utils.AppExt.setupOnBackPressedCallback

class ViewSessionDetailPage : PageFragment() {
    override val controller by viewModels<ViewSessionDetailController>()
    private lateinit var binding: PageViewSessionDetailBinding

    override fun getLayoutId() = R.layout.page_view_session_detail

    override fun initView(rootView: View, isRestore: Boolean) {
        binding = PageViewSessionDetailBinding.bind(rootView)
        binding.root.onClickSafely {}
        requireActivity().setupOnBackPressedCallback {
            val currentPage = requireActivity().getPreviousFragment()
            if (currentPage is ViewSessionPage) {
                requireActivity().supportFragmentManager.popBackStack()
                currentPage.refresh()
            }
        }
    }
}