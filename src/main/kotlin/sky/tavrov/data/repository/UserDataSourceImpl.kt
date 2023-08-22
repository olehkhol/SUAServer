package sky.tavrov.data.repository

import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.setValue
import sky.tavrov.domain.model.User
import sky.tavrov.domain.repository.UserDataSource

class UserDataSourceImpl(
    database: CoroutineDatabase
) : UserDataSource {

    private val users = database.getCollection<User>()

    override suspend fun getUserInfo(userId: String): User? =
        users.findOne(filter = User::id eq userId)

    override suspend fun saveUserInfo(user: User): Boolean =
        if (getUserInfo(user.id) == null) {
            users.insertOne(document = user).wasAcknowledged()
        } else {
            true
        }

    override suspend fun deleteUser(userId: String): Boolean =
        users.deleteOne(filter = User::id eq userId).wasAcknowledged()

    override suspend fun updateUser(
        userId: String,
        firstName: String,
        lastName: String
    ): Boolean =
        users.updateOne(
            filter = User::id eq userId,
            update = setValue(property = User::name, value = "$firstName $lastName")
        ).wasAcknowledged()
}