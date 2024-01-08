package com.atc.team10.attendancetracking.external

import android.app.Application
import com.atc.team10.attendancetracking.utils.PrefUtils

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        PrefUtils.init(this)
    }
}