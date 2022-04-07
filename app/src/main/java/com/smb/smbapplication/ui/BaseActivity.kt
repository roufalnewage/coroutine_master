package com.smb.smbapplication.ui
/**
 * Created by Shijil Kadambath on 03/08/2018
 * for NewAgeSMB
 * Email : shijil@newagesmb.com
 */

import android.content.*
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.smb.smbapplication.R
import com.smb.smbapplication.utils.AppConstants
import com.smb.smbapplication.utils.SessionUtils
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

open class BaseActivity : AppCompatActivity(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>
    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        LocalBroadcastManager.getInstance(this).registerReceiver(
            mMessageReceiver,
            IntentFilter(Intent.ACTION_MAIN)
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver)
    }

    private val mMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            onLocalBroadcastReceiver(intent)
        }
    }

    fun onLocalBroadcastReceiver(intent: Intent) {
        // Get extra data included in the Intent
        val status = intent.getIntExtra(AppConstants.INTENT_BROADCAST, -1)
        if (status == AppConstants.BC_AUTH_LOGOUT) {
            if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {

                val builder = AlertDialog.Builder(this)
                builder.setMessage("Oops!.Your session has been expired.Please login again")
                builder.setCancelable(false)
                builder.setOnCancelListener { dialog: DialogInterface ->
                    dialog.dismiss()
                    finish()
                }
                builder.setPositiveButton(R.string.ok) { dialogInterface: DialogInterface, i: Int -> dialogInterface.dismiss() }
                builder.setOnDismissListener { dialog: DialogInterface ->
                    dialog.dismiss()
                    finish()
                }
                builder.show()
                SessionUtils.clearSession()

            } else {
                finish()
                SessionUtils.clearSession()
            }
        }
    }

}
