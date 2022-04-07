package com.smb.smbapplication.ui.login
/**
 * Created by Shijil Kadambath on 03/08/2018
 * for NewAgeSMB
 * Email : shijil@newagesmb.com
 */

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController

import com.smb.smbapplication.R
import com.smb.smbapplication.di.Injectable
import com.smb.smbapplication.ui.BaseFragment

private const val TAG = "RegistrationStepOneFragment"

/**
 * A simple [Fragment] subclass.
 *
 */
class RegistrationFragment : BaseFragment() {

    override fun getLayoutId()= R.layout.fragment_registration


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    fun navController() = findNavController()
}
