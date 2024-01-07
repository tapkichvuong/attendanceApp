package com.atc.team10.attendancetracking.external

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.atc.team10.attendancetracking.R
import com.atc.team10.attendancetracking.external.ui.page.LoginPage
import com.atc.team10.attendancetracking.external.ui.page.PageFragment
import com.atc.team10.attendancetracking.utils.AppExt.getTopFragment
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

    private fun handleBackPressed() {
        var currentPage = getTopFragment()
        if (currentPage is LoginPage || currentPage !is PageFragment) {
            finish()
        }
        // if is Teacher or Student fragment -> back to exist or show dialog

//        if (currFragment is BoosterFragment) {
//            if (DialogUtils.checkNeedPushRate(this)) {
//                DialogUtils.showRateUsDialog(
//                    supportFragmentManager,
//                    backFromHomePage = true,
//                    allowCancellable = false
//                ) { super.onBackPressed() }
//            } else {
//                super.onBackPressed()
//            }
//        } else if (currFragment is ToolkitFragment || currFragment is SettingFragment) {
//            loadFragment(mListBottomNavigation.get(R.id.booster))
//            mController.mCurrentId = R.id.booster
//            bottomNavigationView.selectedItemId = R.id.booster
//        } else {
//            if (!(currFragment is PageFragment) || currFragment.onBackPressed()) {
//                if (supportFragmentManager.fragments.size > 0) {
//                    currFragment = getTopFragment()
//                    if (currFragment is ToolkitFragment) {
//                        currFragment.refresh()
//                    }
//                }
//                if (DialogUtils.checkNeedPushRate(this)) {
//                    DialogUtils.showRateUsDialog(
//                        supportFragmentManager,
//                        backFromHomePage = false,
//                        allowCancellable = true
//                    ) {}
//                }
//            }
//        }
    }

    private fun loadLoginPage() {
        PageUtils.addFragment(
            this,
            LoginPage(),
            false
        )
    }
}