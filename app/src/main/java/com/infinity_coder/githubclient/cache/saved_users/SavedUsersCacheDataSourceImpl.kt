package com.infinity_coder.githubclient.cache.saved_users

import com.infinity_coder.githubclient.cache.saved_users.dao.RepoEntityDao
import com.infinity_coder.githubclient.cache.saved_users.dao.UserEntityDao
import com.infinity_coder.githubclient.cache.saved_users.dao.UserWithReposEntityDao
import com.infinity_coder.githubclient.data.base.model.User
import com.infinity_coder.githubclient.data.base.model.UserWithRepos
import com.infinity_coder.githubclient.data.saved_users.source.SavedUsersCacheDataSource
import com.infinity_coder.githubclient.presentation.base.ImageStorageManager
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class SavedUsersCacheDataSourceImpl @Inject constructor(
    private val userDao: UserEntityDao,
    private val repoDao: RepoEntityDao,
    private val userWithReposDao: UserWithReposEntityDao,
    private val imageStorageManager: ImageStorageManager
) : SavedUsersCacheDataSource {

    override fun getUserList(usernameStart: String): Single<List<User>> =
        userDao.getUsersByUsernameBegin(usernameStart)

    override fun getSavedUserWithRepos(username: String): Single<UserWithRepos> =
        Single.zip(
            userDao.getUserByUsername(username),
            repoDao.getAllReposByOwnerUsername(username),
            BiFunction { user, repos ->
                UserWithRepos(user, repos)
            }
        )

    override fun cacheUser(userWithRepos: UserWithRepos): Completable =
        Completable.create {
            userWithRepos.user.saveAvatarToFile()
            userWithReposDao.insertUserWithRepos(userWithRepos)
            it.onComplete()
        }

    private fun User.saveAvatarToFile() {
        avatarUrl = imageStorageManager.saveImage(avatarUrl, login)
    }

    override fun removeCachedUser(user: User): Completable =
        Completable.create { emitter ->
            user.removeAvatarFromFile()
            userWithReposDao.deleteUserWithRepos(user)
            emitter.onComplete()
        }

    private fun User.removeAvatarFromFile() {
        val isRemoved = imageStorageManager.removeImage(avatarUrl)
        if (isRemoved) avatarUrl = null
    }

    override fun hasUser(user: User): Single<Boolean> =
        userDao.usersCount(user.login)
            .map { it > 0 }
}