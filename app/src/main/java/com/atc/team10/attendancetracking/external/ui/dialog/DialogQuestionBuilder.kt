package com.atc.team10.attendancetracking.external.ui.dialog

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import com.atc.team10.attendancetracking.R
import com.atc.team10.attendancetracking.databinding.DialogQuestionBinding
import com.atc.team10.attendancetracking.utils.AppExt.onClick

class DialogQuestionBuilder(val context: Context) {
    private val dialog: Dialog = Dialog(context, R.style.CustomDialog)
    private val binding: DialogQuestionBinding
    private var onPositiveClick: () -> Unit = {}
    private var onNegativeClick: () -> Unit = {}

    init {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DialogQuestionBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(binding.root)
        dialog.setCanceledOnTouchOutside(false)
        binding.btnNegative.onClick {
            onNegativeClick()
            dialog.dismiss()
        }
        binding.btnPositive.onClick {
            onPositiveClick()
            dialog.dismiss()
        }
    }

    fun setTitle(title: String): DialogQuestionBuilder {
        binding.tvTitle.text = title
        binding.tvTitle.visibility = View.VISIBLE
        return this
    }

    fun setTitle(title: Int): DialogQuestionBuilder {
        binding.tvTitle.text = context.getString(title)
        binding.tvTitle.visibility = View.VISIBLE
        return this
    }

    fun setMessage(title: String): DialogQuestionBuilder {
        binding.tvMessage.text = title
        return this
    }

    fun setMessage(title: Int): DialogQuestionBuilder {
        binding.tvMessage.text = context.getString(title)
        return this
    }

    fun setPositiveButton(
        buttonName: Int,
        onPositiveClick: () -> Unit = {}
    ): DialogQuestionBuilder {
        binding.btnPositive.text = context.getString(buttonName)
        this.onPositiveClick = onPositiveClick
        return this
    }

    fun setPositiveButton(
        buttonName: String,
        onPositiveClick: () -> Unit = {}
    ): DialogQuestionBuilder {
        binding.btnPositive.text = buttonName
        this.onPositiveClick = onPositiveClick
        return this
    }

    fun setNegativeButton(
        buttonName: Int,
        onNegativeClick: () -> Unit = { }
    ): DialogQuestionBuilder {
        binding.btnNegative.text = context.getString(buttonName)
        this.onNegativeClick = onNegativeClick
        return this
    }

    fun setNegativeButton(
        buttonName: String,
        onNegativeClick: () -> Unit = { }
    ): DialogQuestionBuilder {
        binding.btnNegative.text = buttonName
        this.onNegativeClick = onNegativeClick
        return this
    }

    fun setNegativeButton(buttonName: Int): DialogQuestionBuilder {
        binding.btnNegative.text = context.getString(buttonName)
        return this
    }

    fun setNegativeButton(buttonName: String): DialogQuestionBuilder {
        binding.btnNegative.text = buttonName
        return this
    }


    fun setOnDismissListener(onDismiss: () -> Unit): DialogQuestionBuilder {
        dialog.setOnDismissListener {
            onDismiss()
        }
        return this
    }

    fun build(): Dialog {
        return dialog
    }
}