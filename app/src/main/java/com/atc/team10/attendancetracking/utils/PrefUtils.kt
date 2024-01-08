package com.atc.team10.attendancetracking.utils

import android.content.Context
import android.content.SharedPreferences

object PrefUtils {
    private const val PREFERENCES_NAME = "ATTENDANCE_TRACKING_APP"
    private var sharePref: SharedPreferences? = null
    fun init(context: Context) {
        if (sharePref == null) {
            sharePref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        }
    }

    fun setValue(keyWord: String, value: Any?) {
        val editor = sharePref?.edit()
        when (value) {
            is Int -> editor?.putInt(keyWord, value)
            is Float -> editor?.putFloat(keyWord, value)
            is Long -> editor?.putLong(keyWord, value)
            is Boolean -> editor?.putBoolean(keyWord, value)
            is String -> editor?.putString(keyWord, value)
        }
        editor?.apply()
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getValue(keyWord: String, defaultValue: T): T {
        return when (defaultValue) {
            is Int -> sharePref?.getInt(keyWord, defaultValue) as T
            is Long -> sharePref?.getLong(keyWord, defaultValue) as T
            is Float -> sharePref?.getFloat(keyWord, defaultValue) as T
            is Boolean -> sharePref?.getBoolean(keyWord, defaultValue) as T
            is String -> sharePref?.getString(keyWord, defaultValue) as T
            else -> return defaultValue
        }
    }

    var rememberPassword: Boolean
        get() = getValue("remember_password", false)
        set(value) = setValue("remember_password", value)

    var userCode: String
        get() = getValue("user_code", "")
        set(value) = setValue("user_code", value)

    var userPassword: String
        get() = getValue("user_password", "")
        set(value) = setValue("user_password", value)

    var userToken: String
        get() = getValue("user_token", "")
        set(value) = setValue("user_token", value)
}