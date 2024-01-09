package com.atc.team10.attendancetracking.external.ui.page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.atc.team10.attendancetracking.external.controller.AbsController
import com.atc.team10.attendancetracking.external.ui.dialog.LoadingDialog

abstract class PageFragment : Fragment() {
    abstract val controller: AbsController
    private var loadingDialog: LoadingDialog? = null
    protected open var backPressedTime: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(getLayoutId(), container, false)
        rootView?.let { initView(it, savedInstanceState != null) }
        return rootView
    }

    abstract fun getLayoutId() : Int

    abstract fun initView(rootView: View, isRestore: Boolean = false)

    open fun refresh(){
    }

    protected open fun showDialogLoading(isShow: Boolean, loadingText: String) {
        if (loadingDialog == null) {
            loadingDialog = LoadingDialog(requireContext())
        }
        if (isShow) {
            loadingDialog!!.setTextLoading(loadingText)
            if (!loadingDialog!!.isShowing) loadingDialog!!.show()
        } else {
            if (loadingDialog!!.isShowing) loadingDialog!!.dismiss()
        }
    }
}