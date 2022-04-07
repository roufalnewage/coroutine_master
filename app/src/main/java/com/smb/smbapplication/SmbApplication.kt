package com.smb.smbapplication

/**
 * Created by Shijil Kadambath on 03/08/2018
 * for NewAgeSMB
 * Email : shijil@newagesmb.com
 */

import android.app.Activity
import android.app.Application
import com.smb.smbapplication.di.AppInjector
import com.smb.smbapplication.utils.SessionUtils
import com.smb.smbapplication.utils.logger.Log
import com.smb.smbapplication.utils.logger.LogWrapper
import com.squareup.leakcanary.LeakCanary
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class SmbApplication : Application(), HasAndroidInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>



    override fun onCreate() {
        super.onCreate()
        SessionUtils.init(applicationContext)
        AppInjector.init(this)
        Log.logNode = LogWrapper() // Initialise logging



        if (!LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            // return;
            LeakCanary.install(this)
        }

    }

    override fun androidInjector(): AndroidInjector<Any> =  dispatchingAndroidInjector
}
