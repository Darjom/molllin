package com.mollin.app.data.user

class UserRepository(private val dao: UserDao) {
    suspend fun insert(user: UserEntity) = dao.insert(user)
    suspend fun getUser(uid: String): UserEntity? = dao.getUserByUid(uid)
}
