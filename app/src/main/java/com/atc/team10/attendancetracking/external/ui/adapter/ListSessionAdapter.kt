package com.atc.team10.attendancetracking.external.ui.adapter

import com.atc.team10.attendancetracking.R
import com.atc.team10.attendancetracking.data.model.response.SessionResponse
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ListSessionAdapter : BaseQuickAdapter<SessionResponse, BaseViewHolder>(R.layout.item_session) {
    override fun convert(holder: BaseViewHolder, item: SessionResponse) {
        val timeStart = LocalDateTime.parse(item.timeStart, DateTimeFormatter.ISO_DATE_TIME).format(DateTimeFormatter.ofPattern("HH:mm"))
        val timeEnd = LocalDateTime.parse(item.timeEnd, DateTimeFormatter.ISO_DATE_TIME).format(DateTimeFormatter.ofPattern("HH:mm"))
        holder.setText(R.id.subjectName, item.subjectName)
            .setText(R.id.lessonName, item.lessonName)
            .setText(R.id.time, "$timeStart - $timeEnd")
    }
}