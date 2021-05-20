package com.docusign.sdksamplekotlin.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import java.io.InputStream
import java.io.OutputStream
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.IOException
import java.io.FileOutputStream

object Utils {

    private val TAG = Utils::class.java.simpleName
    private const val BUFFER_SIZE = 1024 * 2

    @Suppress("DEPRECATION")
    fun isNetworkAvailable(context: Context): Boolean {
        var result = false
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            connectivityManager.run {
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)?.run {
                    result = when {
                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI_AWARE) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_LOWPAN) -> true
                        else -> false
                    }
                }
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    if (type == ConnectivityManager.TYPE_WIFI || type == ConnectivityManager.TYPE_MOBILE) {
                        result = true
                    }
                }
            }
        }
        return result
    }

    fun convertAssetToFile(context: Context, assetFileName: String, filePath: String): File? {
        try {
            val inputStream = context.assets.open(assetFileName)
            val newFile = File(filePath)
            val outputStream = FileOutputStream(newFile)
            copy(inputStream, outputStream)
            return newFile
        } catch (exception: IOException) {
                Log.e(TAG, exception.message!!)
        }
        return null
    }

    private fun copy(input: InputStream, output: OutputStream): Int {
        val buffer = ByteArray(BUFFER_SIZE)
        val inputStream = BufferedInputStream(input, BUFFER_SIZE)
        val outputStream = BufferedOutputStream(output, BUFFER_SIZE)
        var count = 0
        var n = 0
        try {
            while (inputStream.read(buffer, 0, BUFFER_SIZE).also { n = it } != -1) {
                outputStream.write(buffer, 0, n)
                count += n
            }
            outputStream.flush()
        } finally {
            try {
                inputStream.close()
                outputStream.close()
            } catch (exception: IOException) {
                Log.e(TAG, exception.message!!)
            }
        }
        return count
    }
}
