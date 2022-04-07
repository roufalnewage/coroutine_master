package com.smb.smbapplication.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.smb.smbapplication.common.autoCleared

/**
 * Created by Shijil Kadambath on 03/08/2018
 * for bigtime
 * Email : shijilkadambath@gmail.com
 */


/**
 * A generic Fragment   .
 * @param <T> The type of the ViewDataBinding.
*/

abstract class BaseDataFragment< T : ViewDataBinding> : BaseFragment() {


    var mBinding by autoCleared<T>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, getLayoutId(),
                container, false, dataBindingComponent)

        return mBinding?.root
    }

}
