package com.smb.smbapplication.ui.home



/**
 * Created by Shijil Kadambath on 03/08/2018
 * for NewAgeSMB
 * Email : shijil@newagesmb.com
 */


import android.content.Intent
import android.os.Bundle
import com.smb.smbapplication.R
import com.smb.smbapplication.ui.BaseActivity
import com.smb.smbapplication.ui.login.LoginActivity
import com.smb.smbapplication.utils.SessionUtils

class HomeActivity : BaseActivity()  {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(!SessionUtils.hasSession()){
            startActivity(Intent(applicationContext, LoginActivity::class.java))
            finishAffinity()
            return
        }
        setContentView(R.layout.activity_home)
    }


}
