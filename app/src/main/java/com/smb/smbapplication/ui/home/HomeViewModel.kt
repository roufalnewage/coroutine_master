package com.smb.smbapplication.ui.home
/**
 * Created by Shijil Kadambath on 03/08/2018
 * for NewAgeSMB
 * Email : shijil@newagesmb.com
 */
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.smb.smbapplication.common.AbsentLiveData
import com.smb.smbapplication.data.api.BaseResponse
import com.smb.smbapplication.data.api.Resource
import com.smb.smbapplication.data.model.LoginSession
import com.smb.smbapplication.data.model.User
import com.smb.smbapplication.repo.UMSRepository
import com.smb.smbapplication.utils.SessionUtils
import java.util.HashMap
import javax.inject.Inject

class HomeViewModel
@Inject constructor(val repoRepository: UMSRepository) : ViewModel() {
    fun clearDB()= repoRepository.clearDB()
    /* login*/
    val usersRepositories= repoRepository .loadUsers()


}