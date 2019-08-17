package com.infinity_coder.githubclient.cache.user

import com.infinity_coder.githubclient.cache.user.model.UserEntity
import com.infinity_coder.githubclient.cache.user.model.UserWithReposEntity
import com.infinity_coder.githubclient.cache.user.dao.UserEntityDao
import com.infinity_coder.githubclient.data.users.cached_users.source.user.UsersCachedDataSource
import com.infinity_coder.githubclient.presentation.base.ImageStorageManager
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class UsersCachedDataSourceImpl @Inject constructor(
    private val userDao: UserEntityDao,
    private val imageStorageManager: ImageStorageManager
) : UsersCachedDataSource {

    override fun getUserList(usernameStart: String): Single<List<UserEntity>> =
        userDao.getUsersByUsernameBegin(usernameStart)

    override fun getUserWithRepos(username: String): Single<UserWithReposEntity> =
        Single.zip(
            userDao.getUserByUsername(username),
            userDao.getAllReposByOwnerUsername(username),
            BiFunction { user, repos ->
                UserWithReposEntity(user, repos)
            }
        )

    override fun cacheUser(userWithRepos: UserWithReposEntity): Completable {
        return Completable.create {
            userWithRepos.user.saveAvatarToFile()
            userDao.insertUserWithRepos(userWithRepos)
            it.onComplete()
        }
    }

    private fun UserEntity.saveAvatarToFile() {
        avatarUrl = imageStorageManager.saveImage(avatarUrl, login)
    }

    override fun removeCachedUser(user: UserEntity): Completable =
        Completable.create { emitter ->
            user.removeAvatarFromFile()
            userDao.deleteUserWithRepos(user)
            emitter.onComplete()
        }

    private fun UserEntity.removeAvatarFromFile() {
        val isRemoved = imageStorageManager.removeImage(avatarUrl)
        if (isRemoved) avatarUrl = null
    }

    override fun hasUser(user: UserEntity): Single<Boolean> =
        userDao.getAllUsers()
            .map { userList ->
                userList.map { user ->
                    user.id
                }
            }.map { userIdList ->
                userIdList.contains(user.id)
            }
}