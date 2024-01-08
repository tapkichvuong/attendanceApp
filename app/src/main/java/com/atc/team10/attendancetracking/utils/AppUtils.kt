package com.atc.team10.attendancetracking.utils

import android.content.Context
import android.net.ConnectivityManager
import android.widget.Toast
import androidx.core.util.Pair
import com.atc.team10.attendancetracking.utils.AppExt.showShortToast
import com.google.gson.Gson
import java.nio.charset.StandardCharsets
import java.util.Base64

object AppUtils {
    fun decodeJwtToken(jwt: String): Pair<String, String> {
        val tokens = jwt.split(".")
        val payload = decodeBase64(tokens[1])
        val jwtClaims = Gson().fromJson(payload, JwtClaims::class.java)
        return Pair(jwtClaims.sub, jwtClaims.role)
    }

    data class JwtClaims(val role: String, val sub: String, val iat: Long, val exp: Long)

    private fun decodeBase64(encoded: String): String {
        val decodedBytes = Base64.getDecoder().decode(encoded)
        return String(decodedBytes, StandardCharsets.UTF_8)
    }
}