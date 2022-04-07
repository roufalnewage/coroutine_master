package com.smb.smbapplication.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingComponent
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.transition.ChangeBounds
import com.google.android.material.snackbar.Snackbar
import com.smb.smbapplication.R
import com.smb.smbapplication.binding.FragmentDataBindingComponent
import com.smb.smbapplication.di.Injectable
import javax.inject.Inject

/**
 * Created by Shijil Kadambath on 03/08/2018
 * for bigtime
 * Email : shijilkadambath@gmail.com
 */


/**
 * A generic Fragment   .
 * @param <T> The type of the ViewDataBinding.
*/

abstract class BaseFragment: Fragment() , Injectable {

    @LayoutRes
    abstract fun getLayoutId(): Int

    var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    @Inject
    protected lateinit var mViewModelFactory: ViewModelProvider.Factory

    override fun onStart() {
        super.onStart()
        sharedElementEnterTransition = ChangeBounds().apply { duration = 400 }
        sharedElementReturnTransition = ChangeBounds().apply { duration = 400 }
    }
    override fun onResume() {
        super.onResume()
        hideKeyboard()
    }

    fun <V : ViewModel> getViewModel(clazz: Class<V>): V {
       //eturn  ViewModelProviders.of(this, mViewModelFactory).get(clazz)
       return  ViewModelProvider(this, mViewModelFactory).get(clazz)

    }

    fun showSnackBar(message: String?, duration: Int = Snackbar.LENGTH_LONG) {
        message?.let {
            Snackbar.make(requireView(), it, duration).show()
        }

    }

    fun showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(requireView().context, message, duration).show()
    }
    fun hideKeyboard() {
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

   /* open fun showPermissionSettingsDialog(requstCode: Int, message: String?) {
        AlertDialog.Builder(requireActivity()) //.setTitle("Delete entry")
                .setCancelable(true)
                .setMessage(message)
                .setPositiveButton(R.string.settings) { dialog, which -> gotoPermissionSettings(requstCode) }
                .setNegativeButton(R.string.cancel, null)
                //.setIcon(R.drawable.ic_dialog_alert)
                .show()
    }*/
    private  fun gotoPermissionSettings(requstCode: Int) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", requireActivity().packageName, null)
        intent.data = uri
        startActivityForResult(intent, requstCode)
    }
}
