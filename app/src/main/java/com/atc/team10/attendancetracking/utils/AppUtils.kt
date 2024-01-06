package com.atc.team10.attendancetracking.utils

import android.content.Context
import android.net.ConnectivityManager
import androidx.core.util.Pair
import java.nio.charset.StandardCharsets
import java.util.Base64

object AppUtils {
    fun isConnectionAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = connectivityManager.activeNetworkInfo
        return netInfo != null && netInfo.isConnected
    }

    fun decodeJwtToken(jwt: String): Pair<String, String> {
        val tokens = jwt.split(".")
        val header = decodeBase64(tokens[0])
        val payload = decodeBase64(tokens[1])
        return Pair(header, payload)
    }

    private fun decodeBase64(encoded: String): String {
        val decodedBytes = Base64.getDecoder().decode(encoded)
        return String(decodedBytes, StandardCharsets.UTF_8)
    }
}