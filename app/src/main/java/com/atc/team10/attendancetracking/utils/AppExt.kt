package com.atc.team10.attendancetracking.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData

object AppExt {
    private var toast: Toast? = null

    fun Context.showShortToast(message: String) {
        toast?.cancel()
        toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        toast?.show()
    }

    fun Context.showLongToast(message: Int) {
        toast?.cancel()
        toast = Toast.makeText(this, message, Toast.LENGTH_LONG)
        toast?.show()
    }

    fun Context.isConnectionAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = connectivityManager.activeNetworkInfo
        val hasConnection = netInfo != null && netInfo.isConnected
        if (!hasConnection) {
            showShortToast("Network unavailable.")
        }
        return hasConnection
    }

    fun Context.isCameraPermisionGranted(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            android.Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun FragmentActivity.getTopFragment(): Fragment? {
        val size = supportFragmentManager.fragments.size
        if (size > 0) {
            return supportFragmentManager.fragments[size - 1]
        }
        return null
    }

    fun FragmentActivity.getPreviousFragment(): Fragment? {
        val size = supportFragmentManager.fragments.size
        if (size > 1) {
            return supportFragmentManager.fragments[size - 2]
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

    fun FragmentActivity.setupOnBackPressedCallback(onBackPressedAction: () -> Unit): OnBackPressedCallback {
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressedAction.invoke()
            }
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        return onBackPressedCallback
    }


    fun<T> MutableLiveData<T>.updateValue(newValue: T){
        if(Looper.myLooper() == Looper.getMainLooper()){
            value = newValue
        } else{
            postValue(newValue)
        }
    }

    fun View.onClick(onClick: ((View?) -> Unit)? = null) {
        val listener = SafeOnClickListener()
        listener.onSafeClick = onClick
        this.onClickAffect(listener)
    }

    fun View.onClickSafely(onClick: ((View?) -> Unit)? = null) {
        val listener = SafeOnClickListener()
        listener.onSafeClick = onClick
        this.setOnClickListener(listener)
    }

    @SuppressLint("ClickableViewAccessibility")
    fun View.onClickAffect(onClick: View.OnClickListener) {
        this.setOnTouchListener { v, motionEvent ->
            when (motionEvent?.action) {
                MotionEvent.ACTION_DOWN -> {
                    v?.scaleX = 0.97f
                    v?.scaleY = 0.97f
                }

                MotionEvent.ACTION_CANCEL,
                MotionEvent.ACTION_UP -> {
                    v?.scaleX = 1f
                    v?.scaleY = 1f
                }
            }
            false
        }
        this.setOnClickListener(onClick)
    }

    fun View?.visible() {
        this?.visibility = View.VISIBLE
    }

    fun View?.gone() {
        this?.visibility = View.GONE
    }

    fun View?.invisible() {
        this?.visibility = View.INVISIBLE
    }

    fun View?.setVisible(isVisible: Boolean) {
        this?.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    fun View?.isShow(): Boolean {
        return this?.visibility == View.VISIBLE
    }

    fun View?.isGone(): Boolean {
        return this?.visibility == View.GONE
    }

    fun View?.enable() {
        this?.apply {
            isEnabled = true
            alpha = 1f
        }
    }

    fun View?.disable() {
        this?.apply {
            isEnabled = false
            alpha = 0.4f
        }
    }

    fun View?.setEnable(isEnable: Boolean) {
        this?.apply {
            isEnabled = isEnable
            alpha = if (isEnable) 1f else 0.4f
        }
    }

    fun sendLog(log: String) {
        Log.d("duc.nh3", log)
    }

    fun Long.bytesToKB(): Long {
        return this / 1024
    }
}