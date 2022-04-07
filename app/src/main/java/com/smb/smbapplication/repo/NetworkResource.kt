package com.smb.smbapplication.repo

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.smb.smbapplication.AppExecutors
import com.smb.smbapplication.data.api.*

/**
 * Created by Shijil Kadambath on 03/08/2018
 * for NewAgeSMB
 * Email : shijil@newagesmb.com
 */


/**
 * A generic class that can provide a resource backed by both the sqlite database and the network.
 *
 *
 * You can read more about it in the [Architecture
 * Guide](https://developer.android.com/arch).
 * @param <ResultType>
 * @param <RequestType>
</RequestType></ResultType> */
abstract class NetworkResource<ResultType>
@MainThread constructor(private val appExecutors: AppExecutors) {

    private val result = MediatorLiveData<Resource<ResultType>>()

    init {
        result.value = Resource.loading(null)


        fetchFromNetwork()
    }

    @MainThread
    private fun setValue(newValue: Resource<ResultType>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }
    private fun fetchFromNetwork() {
        val apiResponse = createCall()
        result.addSource(apiResponse) { response ->
            result.removeSource(apiResponse)
            when (response) {
                is ApiSuccessResponse -> {
                    appExecutors.mainThread().execute {
                        // we specially request a new live data,
                        // otherwise we will get immediately last cached value,
                        // which may not be updated with latest results received from network.
                        setValue(Resource.success(processResponse(response)))
                            appExecutors.diskIO().execute {
                                saveCallResult(processResponse(response))
                            }
                        }
                }
                is ApiEmptyResponse -> {
                    appExecutors.mainThread().execute {
                        setValue(Resource.success(null))
                    }
                }
                is ApiErrorResponse -> {
                   // onFetchFailed()
                    setValue(Resource.error(response.errorCode,response.errorMessage, response.body))
                }
            }
        }
    }

   // protected open fun onFetchFailed() {}

    fun asLiveData() = result as LiveData<Resource<ResultType>>

    @WorkerThread
    protected open fun processResponse(response: ApiSuccessResponse<ResultType>) = response.body


    @WorkerThread
    protected  open fun saveCallResult(item: ResultType){

    }


    @MainThread
    protected abstract fun createCall(): LiveData<ApiResponse<ResultType>>
}