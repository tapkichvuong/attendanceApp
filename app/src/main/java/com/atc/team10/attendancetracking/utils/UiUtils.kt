package com.atc.team10.attendancetracking.utils

import android.os.SystemClock

object UiUtils {
    private const val DOUBLE_CLICK_TIME_DELTA: Long = 400

    private var sLastClickTime: Long = 0
    private var sPrevId = 0
    private var sPrevPosition = 0

    fun isValidClick(id: Int): Boolean {
        return isValidClick(id, -1, DOUBLE_CLICK_TIME_DELTA)
    }

    fun isValidClick(id: Int, position: Int): Boolean {
        return position >= 0 && isValidClick(id, position, DOUBLE_CLICK_TIME_DELTA)
    }

    fun isValidClick(id: Int, position: Int, delay: Long): Boolean {
        var bRet = true
        val clickTime = SystemClock.elapsedRealtime()
        if (sPrevId == id && sPrevPosition == position) {
            if (clickTime - sLastClickTime < delay) {
                bRet = false
            }
        } else {
        }

        sLastClickTime = clickTime
        sPrevId = id
        sPrevPosition = position
        return bRet
    }
}