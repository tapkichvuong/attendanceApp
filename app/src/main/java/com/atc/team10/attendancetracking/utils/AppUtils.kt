package com.atc.team10.attendancetracking.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.core.util.Pair
import com.google.gson.Gson
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
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

    fun resizeBitmap(context: Context, uri: Uri, maxSizeInBytes: Int): Bitmap? {
        var inputStream: InputStream? = null
        try {
            // Open an InputStream from the URI
            inputStream = context.contentResolver.openInputStream(uri)

            // Decode the input stream into a Bitmap
            val originalBitmap = BitmapFactory.decodeStream(inputStream)

            // Calculate the quality to keep the size under the desired limit
            var quality = 100
            var byteArrayOutputStream = ByteArrayOutputStream()
            originalBitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream)
            while (byteArrayOutputStream.toByteArray().size > maxSizeInBytes) {
                quality -= 10
                byteArrayOutputStream = ByteArrayOutputStream()
                originalBitmap.compress(Bitmap.CompressFormat.PNG, quality, byteArrayOutputStream)
            }

            // Decode the compressed byte array into a resized Bitmap
            return BitmapFactory.decodeByteArray(
                byteArrayOutputStream.toByteArray(),
                0,
                byteArrayOutputStream.toByteArray().size
            )
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            // Close the InputStream
            inputStream?.close()
        }
        return null
    }

    fun saveBitmapToFile(context: Context, bitmap: Bitmap, fileName: String): File? {
        val file = File(context.externalCacheDir, fileName)
        return try {
            val stream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream) // You can choose another format and quality here
            stream.flush()
            stream.close()
            file
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    fun imageToBase64(filePath: String?): String {
        val path: Path = Paths.get(filePath)
        val imageBytes = Files.readAllBytes(path)
        val base64Encoded: ByteArray = Base64.getEncoder().encode(imageBytes)
        return String(base64Encoded, charset("UTF-8"))
    }
}