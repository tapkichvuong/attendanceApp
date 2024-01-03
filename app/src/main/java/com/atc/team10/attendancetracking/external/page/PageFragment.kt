package com.atc.team10.attendancetracking.external.page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.atc.team10.attendancetracking.external.controller.AbsController

abstract class PageFragment : Fragment() {
    abstract val controller: AbsController

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

    open fun onBackPressed(): Boolean{
        return true
    }
}