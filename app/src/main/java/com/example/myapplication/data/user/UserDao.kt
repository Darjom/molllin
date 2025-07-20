package com.example.myapplication.data.user

import androidx.room.*

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserEntity)

    @Query("SELECT * FROM users WHERE uid = :uid LIMIT 1")
    suspend fun getUserByUid(uid: String): UserEntity?
}
