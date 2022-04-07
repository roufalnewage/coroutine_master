package com.smb.smbapplication.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.smb.smbapplication.data.model.User

/**
 * Interface for database access on Repo related operations.
 */

/**
 * Created by Shijil Kadambath on 03/08/2018
 * for NewAgeSMB
 * Email : shijil@newagesmb.com
 */

@Dao
abstract class UMSDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertUsers(vararg users: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertUsers(users: List<User>)

    @Query(" SELECT * FROM User")
    abstract fun loadUsers(): LiveData<List<User>>


}
