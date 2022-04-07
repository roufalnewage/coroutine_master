package com.smb.smbapplication.data.model

/**
 * Created by Shijil Kadambath on 03/08/2018
 * for NewAgeSMB
 * Email : shijil@newagesmb.com
 */
import androidx.annotation.NonNull
import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(primaryKeys = ["id"])
  data class User(

        @field:SerializedName("full_name")
        var name: String? = null,

        @NonNull
        @field:SerializedName("id")
        var id: Int = 0

)