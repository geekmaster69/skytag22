package com.example.skytah2.login

import android.content.Context
import android.os.Build
import android.provider.Settings
import android.text.TextUtils

class AppUtils(private val appContext: Context) {


    fun getDeviceId(): String{
        return capitalize(Settings.Secure.getString(appContext.contentResolver, Settings.Secure.ANDROID_ID))
    }

    fun getDeviceName(): String {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        return if (model.startsWith(manufacturer)) {
            capitalize(model)
        } else capitalize(manufacturer) + "_" + model
    }

    private fun capitalize(str: String): String {
        if (TextUtils.isEmpty(str)) {
            return str
        }
        val arr = str.toCharArray()
        val phrase = StringBuilder()
        for (c in arr) {
            if (Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c))
                continue
            } else if (Character.isWhitespace(c)) {
                phrase.append("_")
                continue
            }
            phrase.append(c)
        }
        return phrase.toString()
    }
}
