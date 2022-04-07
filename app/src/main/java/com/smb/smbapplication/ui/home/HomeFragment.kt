package com.smb.smbapplication.ui.home

/**
 * Created by Shijil Kadambath on 03/08/2018
 * for NewAgeSMB
 * Email : shijil@newagesmb.com
 */
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.smb.smbapplication.AppExecutors

import com.smb.smbapplication.R
import com.smb.smbapplication.common.autoCleared
import com.smb.smbapplication.data.api.Status
import com.smb.smbapplication.data.model.User
import com.smb.smbapplication.databinding.FragmentHomeBinding
import com.smb.smbapplication.databinding.FragmentLoginBinding
import com.smb.smbapplication.ui.BaseDataFragment
import com.smb.smbapplication.ui.BaseFragment
import com.smb.smbapplication.ui.RetryCallback
import com.smb.smbapplication.ui.login.LoginActivity
import com.smb.smbapplication.utils.CommonUtils
import com.smb.smbapplication.utils.SessionUtils
import com.smb.smbapplication.utils.logger.Log
import javax.inject.Inject

private const val TAG: String = "LoginFragment"

/**
 * A simple [Fragment] subclass.
 *
 */
class HomeFragment : BaseDataFragment<FragmentHomeBinding>() {


    @Inject
    lateinit var appExecutors: AppExecutors

    lateinit var mViewModel: HomeViewModel

    var adapter by autoCleared<ListAdapter>()


    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel = getViewModel(HomeViewModel::class.java)


        adapter = ListAdapter(appExecutors = appExecutors) {

            /*navController().navigate(
                   LoginFragmentDirections.showRegistration()
            )*/
        }

        mBinding.rvList.adapter = adapter

        mBinding.callback = object : RetryCallback {
            override fun retry() {
                loadData()
            }
        }
        loadData()

        mBinding.btnLogout.setOnClickListener {
            logout()
        }
    }

    fun navController() = findNavController()

    fun loadData() {
        mViewModel.usersRepositories.observe(this.viewLifecycleOwner, Observer { response ->
            mBinding.searchResource = response
            if (response == null || response.status == Status.LOADING) {
                return@Observer
            }
            when {
                response.apiData == null -> {
                    showSnackBar(response.message!!)
                }
                response.isSuccess() && response.apiData.hasData() -> {
                    //showSnackBar(response.apiData.message)
                    adapter.submitList(response.apiData.data?.data)
                    adapter.notifyDataSetChanged()
                }
                else -> {
                    showSnackBar(response.apiData.message)
                }
            }
        })
    }

    fun logout() {
        AlertDialog.Builder(requireActivity())
            .setMessage(R.string.alert_logout)
            .setPositiveButton(R.string.logout) { _, i ->
                mViewModel.clearDB()
                SessionUtils.clearSession()
                startActivity(Intent(requireActivity(), LoginActivity::class.java))
                requireActivity().finishAffinity()

            }.setNegativeButton(R.string.cancel) { dialog, i ->

            }.show()
    }

}
