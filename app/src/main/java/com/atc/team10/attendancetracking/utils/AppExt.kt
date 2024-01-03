package com.atc.team10.attendancetracking.utils

import android.os.Looper
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData

object AppExt {
    fun AppCompatActivity.getTopFragment(): Fragment? {
        val size = supportFragmentManager.fragments.size
        if (size > 0) {
            return supportFragmentManager.fragments[size - 1]
        }
        return null
    }

    fun AppCompatActivity.setupOnBackPressedCallback(onBackPressedAction: () -> Unit) {
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressedAction.invoke()
            }
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }


    fun<T> MutableLiveData<T>.updateValue(newValue: T){
        if(Looper.myLooper() == Looper.getMainLooper()){
            value = newValue
        } else{
            postValue(newValue)
        }
    }
}