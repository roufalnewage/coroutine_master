package com.smb.smbapplication.data.api


import android.content.Context
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.smb.smbapplication.di.AppModule
import com.smb.smbapplication.di.AppModule_GetBaseUrlFactory
import com.smb.smbapplication.utils.AppConstants
import com.smb.smbapplication.utils.RequestBodyUtil
import com.smb.smbapplication.utils.SessionUtils
import com.smb.smbapplication.utils.logger.Log
import okhttp3.Interceptor
import okhttp3.Response
import org.json.JSONObject
import java.util.HashMap

/**
 * Created by Shijil Kadambath on 03/08/2018
 * for NewAgeSMB
 * Email : shijil@newagesmb.com
 */


class AuthorizationInterceptor(val context: Context) : Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response {
        var firstRequest = chain.request()

        if (!SessionUtils.authToken.isNullOrEmpty()) {
            firstRequest =
                chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer " + SessionUtils.authToken!!)
                    .build()
        }
        var response = chain.proceed(firstRequest)
        if (response.code == 401 && SessionUtils.hasSession()) {
            response.close()
            val map = HashMap<String, Any?>()
            map["token"] = SessionUtils.authToken!!
            map["refresh_token"] = SessionUtils.refreshToken!!
            val authRequest = firstRequest.newBuilder()
                .post(RequestBodyUtil.getRequestBodyMap(map))
                .url(AppModule_GetBaseUrlFactory.create(AppModule()).get() + "auth/token").build()  // generate new token url
            response = chain.proceed(authRequest)

            if (response.isSuccessful) {
                try {
                    val auth =
                        JSONObject(response.body?.string()).getJSONObject("data").getString("token")
                    SessionUtils.saveAuthToken(auth)
                    response.close()
                    val secondRequest = firstRequest.newBuilder().removeHeader("Authorization")
                        .addHeader("Authorization", "Bearer $auth")
                        .method(firstRequest.method, firstRequest.body).build()
                    response = chain.proceed(secondRequest)

                } catch (e: Exception) {
                    e.printStackTrace()
                }

            } else if (response.code == 401) {
                val intent = Intent(Intent.ACTION_MAIN)
                intent.putExtra(AppConstants.INTENT_BROADCAST, AppConstants.BC_AUTH_LOGOUT)
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
            }
        }

        return response
    }


}
