package com.smb.smbapplication.utils

import com.smb.smbapplication.BuildConfig

/**
 * Created by Shijil Kadambath on 03/08/2018
 * for NewAgeSMB
 * Email : shijil@newagesmb.com
 */
object AppConstants {
    private const val PACKAGE_NAME = BuildConfig.APPLICATION_ID
    const val DATABASE = "app.db"

    const val PREFERENCE_NAME = PACKAGE_NAME+"smb_pref"

    val PRE_SESSION = PACKAGE_NAME+"_session"
    val PRE_AUTH_TOKEN= PACKAGE_NAME+"_authtoken"
    val PRE_REFRESH_TOKEN= PACKAGE_NAME+"_refreshtoken"


    const val INTENT_BROADCAST = PACKAGE_NAME+"smb_local_bc"
    const val BC_AUTH_LOGOUT = 1
}