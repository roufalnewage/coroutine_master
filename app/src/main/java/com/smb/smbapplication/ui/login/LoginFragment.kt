package com.smb.smbapplication.ui.login

/**
 * Created by Shijil Kadambath on 03/08/2018
 * for NewAgeSMB
 * Email : shijil@newagesmb.com
 */
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.smb.smbapplication.AppExecutors

import com.smb.smbapplication.R
import com.smb.smbapplication.common.autoCleared
import com.smb.smbapplication.data.api.Status
import com.smb.smbapplication.data.model.User
import com.smb.smbapplication.databinding.FragmentLoginBinding
import com.smb.smbapplication.ui.BaseDataFragment
import com.smb.smbapplication.ui.RetryCallback
import com.smb.smbapplication.ui.home.ListAdapter
import com.smb.smbapplication.utils.CommonUtils
import com.smb.smbapplication.utils.SessionUtils
import javax.inject.Inject

private const val TAG: String = "LoginFragment"

/**
 * A simple [Fragment] subclass.
 *
 */
class LoginFragment : BaseDataFragment<FragmentLoginBinding>() {


    @Inject
    lateinit var appExecutors: AppExecutors

    lateinit var mViewModel: LoginViewModel


    override fun getLayoutId(): Int {
        return R.layout.fragment_login;
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mBinding.layoutBinder = this
        mViewModel = getViewModel(LoginViewModel::class.java)

        mViewModel.loginRepositories.observe(this.viewLifecycleOwner, Observer { response ->
            mBinding.searchResource = response
            if (response == null || response.status == Status.LOADING) {
                return@Observer
            }
            when {
                response.apiData == null -> {
                    showSnackBar(response.message!!)
                }
                response.isSuccess() && response.apiData.hasData() -> {
                    SessionUtils.saveSession(response.apiData.data!!, context)
                    showSnackBar(response.apiData.message)

                    navController().navigate(LoginFragmentDirections.showHome())
                    requireActivity().finish()
                }
                else -> {
                    showSnackBar(response.apiData.message)
                }
            }
            mViewModel.login(null)
        })

        mBinding.callback = object : RetryCallback {
            override fun retry() {
               login()
            }
        }
    }

    fun navController() = findNavController()


    fun login(){
        hideKeyboard()
        val email = mBinding.txtName.text.toString().trim()
        val password = mBinding.txtPassword.text.toString()

        if (email.isEmpty()) {
            mBinding.txtName.error = getString(R.string.submit)
        }else if (!CommonUtils.isEmailValid(email)){
            mBinding.txtName.error =getString(R.string.alert_email_invalid)
        } else if (password.isEmpty()) {
            mBinding.txtPassword.error = getString(R.string.alert_password_empty)
        } else {
            mViewModel.login(email, password)
        }
    }
}
