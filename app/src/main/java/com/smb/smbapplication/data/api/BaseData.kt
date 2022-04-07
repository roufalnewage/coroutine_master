package com.smb.smbapplication.data.api

/**
 * Created by Shijil Kadambath on 03/08/2018
 * for NewAgeSMB
 * Email : shijil@newagesmb.com
 */

import com.google.gson.annotations.SerializedName


/**
 * Base Gson class structure of all api responses.
 */

data class BaseData<T>(
    @field:SerializedName("users")
    val data: T?,

    )