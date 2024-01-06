package com.atc.team10.attendancetracking.external.ui.dialog

import android.app.Dialog
import android.content.Context
import android.view.Window
import com.atc.team10.attendancetracking.R
import com.atc.team10.attendancetracking.databinding.DialogLoadingBinding

class LoadingDialog(context: Context) : Dialog(context, R.style.CustomDialog) {
    val binding: DialogLoadingBinding

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        binding = DialogLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setCancelable(false)
    }

    fun setTextLoading(content: String) {
        binding.tvLoading.text = content
    }
}