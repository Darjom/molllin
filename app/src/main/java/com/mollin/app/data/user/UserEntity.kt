package com.mollin.app.data.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val uid: String,  // puede ser el uid de FirebaseAuth
    val name: String,
    val phone: String,
    val address: String
)
