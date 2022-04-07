package com.smb.smbapplication.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.smb.smbapplication.data.model.LoginSession

import javax.inject.Singleton

@Singleton
class SessionUtils {
    companion object {

        lateinit var preferences: SharedPreferences

        fun init(context: Context) {
            preferences =
                context.getSharedPreferences(AppConstants.PREFERENCE_NAME, Context.MODE_PRIVATE)
        }

        fun saveSession(session: LoginSession?, context: Context?) {

            //if (session == null) return
            if (preferences == null) {
                preferences = context!!.getSharedPreferences(
                    AppConstants.PREFERENCE_NAME,
                    Context.MODE_PRIVATE
                )
            }
            if (!session?.token.isNullOrEmpty()){
                saveAuthToken(session!!.token!!)
            }
            if (!session?.refreshToken.isNullOrEmpty()){
                saveRefreshToken(session!!.refreshToken!!)
            }

            if (hasSession()) {
                session?.sessionId = loginSession?.sessionId
                if (session?.user==null)session?.user=loginSession?.user
            }

            val prefsEditor = preferences.edit()
            val gson = Gson()
            prefsEditor.putString(AppConstants.PRE_SESSION, gson.toJson(session))
            //val json = gson.toJson(loginSession)
            prefsEditor.apply()
        }


        fun hasSession(): Boolean {
            return loginSession != null
        }

        var loginSession: LoginSession? = null
            get() {
                try {
                    val gson = Gson()
                    val json = preferences.getString(AppConstants.PRE_SESSION, "")
                    return gson.fromJson(json, LoginSession::class.java)
                } catch (e: Exception) {
                    return null
                }

            }

        fun clearSession() {
            preferences.edit()
                .remove(AppConstants.PRE_SESSION)
                .remove(AppConstants.PRE_AUTH_TOKEN)
                .remove(AppConstants.PRE_REFRESH_TOKEN)
                .apply()
            loginSession = null

        }




        val authToken: String?
            get() = preferences.getString(AppConstants.PRE_AUTH_TOKEN, "")
        val refreshToken: String?
            get() = preferences.getString(AppConstants.PRE_REFRESH_TOKEN, "")

        fun saveAuthToken(token: String) {
            preferences.edit().putString(AppConstants.PRE_AUTH_TOKEN, token).apply()
        }
        fun saveRefreshToken(token: String) {
            preferences.edit().putString(AppConstants.PRE_REFRESH_TOKEN, token).apply()
        }
    }
}