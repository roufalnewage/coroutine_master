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

package com.smb.smbapplication.common


import androidx.lifecycle.LiveData
import com.smb.smbapplication.data.api.ApiResponse
import okhttp3.ResponseBody
import retrofit2.*
import java.lang.reflect.Type
import java.util.concurrent.atomic.AtomicBoolean

/**
 * A Retrofit adapter that converts the Call into a LiveData of ApiResponse.
 * @param <R>
</R> */
class LiveDataCallAdapter<R>(private val responseType: Type,val retrofit: Retrofit) :
    CallAdapter<R, LiveData<ApiResponse<R>>> {
    var converter: Converter<ResponseBody?, R> =
        retrofit.responseBodyConverter(responseType, arrayOfNulls<Annotation>(0))

    override fun responseType() = responseType

    override fun adapt(call: Call<R>): LiveData<ApiResponse<R>> {
        return object : LiveData<ApiResponse<R>>() {
            private var started = AtomicBoolean(false)
            override fun onActive() {
                super.onActive()
                if (started.compareAndSet(false, true)) {
                    call.enqueue(object : Callback<R> {
                        override fun onResponse(call: Call<R>, response: Response<R>) {
                            postValue(ApiResponse.create(response,converter))
                        }

                        override fun onFailure(call: Call<R>, throwable: Throwable) {
                            throwable.printStackTrace()
                            postValue(ApiResponse.create(throwable))
                        }
                    })
                }
            }
        }
    }
}
