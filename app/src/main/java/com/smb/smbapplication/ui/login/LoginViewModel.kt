package com.smb.smbapplication.ui.login
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

class LoginViewModel
@Inject constructor( repoRepository: UMSRepository) : ViewModel() {

    /* login*/
    private val login = MutableLiveData<HashMap<String, Any?>>()
    val loginRepositories: LiveData<Resource<BaseResponse<LoginSession>>> = Transformations
        .switchMap(login) { map ->
            if (map == null) {
                AbsentLiveData.create()
            } else {
                repoRepository.login(map)
            }
        }

    fun login(data: HashMap<String, Any?>?) {
        login.value = data
    }

    fun login(email: String, password: String) {
        val data = HashMap<String, Any?>()
        data["username"] = email
        data["password"] = password
        data["platform"] = "ANDROID"
        login(data)
    }

}