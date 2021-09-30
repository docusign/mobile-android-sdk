package com.docusign.sdksamplekotlin.utils

import android.content.Context

object ClientUtils {

    fun setSignedStatus(context: Context, clientPref: String, signed: Boolean = false) {
        val sharedPreferences = context.getSharedPreferences(Constants.APP_SHARED_PREFERENCES,
                Context.MODE_PRIVATE)
        val signedStatus = when (clientPref) {
            Constants.CLIENT_A_PREF -> {
                Constants.CLIENT_A_SIGNED_STATUS
            }
            Constants.CLIENT_B_PREF -> {
                Constants.CLIENT_B_SIGNED_STATUS
            }
            else -> {
                Constants.CLIENT_C_SIGNED_STATUS
            }
        }
        sharedPreferences.edit().putBoolean(signedStatus, signed).apply()
    }

    fun getSignedStatus(context: Context, clientPref: String): Boolean {
        val sharedPreferences = context.getSharedPreferences(Constants.APP_SHARED_PREFERENCES,
                Context.MODE_PRIVATE)
        val signedStatus = if (clientPref == Constants.CLIENT_A_PREF) {
            Constants.CLIENT_A_SIGNED_STATUS
        } else {
            Constants.CLIENT_B_SIGNED_STATUS
        }
        return sharedPreferences.getBoolean(signedStatus, false)
    }
}