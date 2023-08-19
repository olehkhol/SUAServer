package sky.tavrov.domain.repository

import sky.tavrov.domain.model.User

interface UserDataSource {

    suspend fun getUserInfo(userId: String): User?
    suspend fun saveUserInfo(user: User): Boolean
    suspend fun deleteUser(userId: String): Boolean
    suspend fun updateUser(
        userId: String,
        firstName: String,
        lastName: String
    ): Boolean
}