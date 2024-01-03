package com.atc.team10.attendancetracking.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

object PageUtils {
    fun addFragment(activity: FragmentActivity, fragment: Fragment, needDetachCurrent: Boolean = false){
        if(activity.supportFragmentManager.isStateSaved) return
        if(needDetachCurrent){
            activity.supportFragmentManager.popBackStack()
        }
        activity.supportFragmentManager
            .beginTransaction()
            .add(android.R.id.content, fragment)
            .addToBackStack(fragment::class.simpleName)
            .commit()
    }
}