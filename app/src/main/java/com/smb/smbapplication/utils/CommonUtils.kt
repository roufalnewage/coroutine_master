/**
 * Created by Shijil Kadambath on 03/08/2018
 * for NewAgeSMB
 * Email : shijil@newagesmb.com
 */
package com.smb.smbapplication.utils

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import android.util.Patterns
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

object CommonUtils {
    @SuppressLint("all")
    fun getDeviceId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }



    fun isEmailValid(email: String?): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

   
}