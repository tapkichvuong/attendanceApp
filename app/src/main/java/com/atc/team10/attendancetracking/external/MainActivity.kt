package com.atc.team10.attendancetracking.external

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.atc.team10.attendancetracking.R
import com.atc.team10.attendancetracking.external.ui.page.LoginPage
import com.atc.team10.attendancetracking.utils.PageUtils

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
//        setupOnBackPressedCallback {
//            handleBackPressed()
//        }
        loadLoginPage()
    }

    private fun loadLoginPage() {
        PageUtils.addFragment(
            this,
            LoginPage(),
            false
        )
    }
}