package com.atc.team10.attendancetracking.external.ui.adapter

import com.atc.team10.attendancetracking.R
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class ListAbsentAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_student) {
    override fun convert(holder: BaseViewHolder, item: String) {
        holder.setText(R.id.order, (holder.layoutPosition + 1).toString())
            .setText(R.id.studentCode, item)
    }
}