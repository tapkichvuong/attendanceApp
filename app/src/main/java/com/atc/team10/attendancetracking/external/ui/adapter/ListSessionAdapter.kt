package com.atc.team10.attendancetracking.external.ui.adapter

import com.atc.team10.attendancetracking.R
import com.atc.team10.attendancetracking.data.model.response.SessionResponse
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import java.time.format.DateTimeFormatter

class ListSessionAdapter : BaseQuickAdapter<SessionResponse, BaseViewHolder>(R.layout.item_session) {
    override fun convert(holder: BaseViewHolder, item: SessionResponse) {
        val timeStart = DateTimeFormatter.ofPattern("HH:mm").format(item.timeStart)
        val timeEnd = DateTimeFormatter.ofPattern("HH:mm").format(item.timeEnd)
        holder.setText(R.id.subjectName, "Subject ${item.id}")
            .setText(R.id.lessonName, "Lesson ${item.id}")
            .setText(R.id.time, "$timeStart - $timeEnd")
    }
}