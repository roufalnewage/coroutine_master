package com.smb.smbapplication.data.model

/**
 * Created by Shijil Kadambath on 03/08/2018
 * for NewAgeSMB
 * Email : shijil@newagesmb.com
 */
import android.webkit.URLUtil
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

data class      LoginSession(
   @SerializedName("refresh_token")
    var refreshToken: String?,
    @SerializedName("token")
    var token: String?,
    @SerializedName("session_id")
    var sessionId: String?,

    @Expose
    @SerializedName("user") var user: User?,


    ) {



    class User(
        @SerializedName("id") val               id: Int,
    )
}
