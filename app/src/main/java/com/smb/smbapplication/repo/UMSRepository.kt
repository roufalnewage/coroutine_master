/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.smb.smbapplication.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import com.smb.smbapplication.AppExecutors
import com.smb.smbapplication.data.api.BaseData
import com.smb.smbapplication.data.api.BaseResponse
import com.smb.smbapplication.data.api.WebService
import com.smb.smbapplication.data.api.Resource
import com.smb.smbapplication.data.db.AppDb
import com.smb.smbapplication.data.db.UMSDao
import com.smb.smbapplication.data.model.LoginSession
import com.smb.smbapplication.data.model.User
import com.smb.smbapplication.utils.RequestBodyUtil
import java.util.HashMap
import javax.inject.Inject
import javax.inject.Singleton
/**
 * Created by Shijil Kadambath on 03/08/2018
 * for NewAgeSMB
 * Email : shijil@newagesmb.com
 */

/**
 * Repository that handles User instances.
 *
 */
@Singleton
class UMSRepository @Inject constructor(
        private val appExecutors: AppExecutors,
        private val db: AppDb,
        private val umsoDao: UMSDao,
        private val webService: WebService
) {
    fun clearDB(){
        appExecutors.diskIO().execute {
            db.clearAllTables()
        }
    }

    fun login(map: HashMap<String, Any?>): LiveData<Resource<BaseResponse<LoginSession>>> {
        return object : NetworkResource<BaseResponse<LoginSession>>(appExecutors) {
            override fun createCall() = webService.login(RequestBodyUtil.getRequestBodyMap(map))
        }.asLiveData()
    }

    fun loadUsers():  LiveData<Resource<BaseResponse<BaseData<List<User>>>>> {
        return object : NetworkBoundResource<BaseResponse<BaseData<List<User>>>, BaseResponse<BaseData<List<User>>>>(appExecutors) {

            override fun createCall() = webService.loadUsers()
            override fun saveCallResult(item: BaseResponse<BaseData<List<User>>>) {
                if (item.hasData() && item.data?.data!=null) {
                    umsoDao.insertUsers(item.data.data)
                }
            }

            override fun shouldFetch(data: BaseResponse<BaseData<List<User>>>?): Boolean {
                return false
            }

            override fun loadFromDb(): LiveData<BaseResponse<BaseData<List<User>>>> {
                val result = MediatorLiveData<BaseResponse<BaseData<List<User>>>>()
                result.addSource(umsoDao.loadUsers()) { list ->

                    result.setValue(BaseResponse(BaseData(list), "",))
                }

                return  result
            }


        }.asLiveData()
    }



}
