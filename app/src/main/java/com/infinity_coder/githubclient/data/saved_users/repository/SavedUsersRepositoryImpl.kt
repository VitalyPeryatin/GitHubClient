package com.infinity_coder.githubclient.data.saved_users.repository

import com.infinity_coder.githubclient.data.base.model.User
import com.infinity_coder.githubclient.data.base.model.UserWithRepos
import com.infinity_coder.githubclient.data.saved_users.source.SavedUsersCachedDataSourceProxy
import com.infinity_coder.githubclient.domain.saved_users.repository.SavedUsersRepository
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SavedUsersRepositoryImpl @Inject constructor(
    private val cachedDataSource: SavedUsersCachedDataSourceProxy
) : SavedUsersRepository {

    override fun addCachedUser(userWithRepos: UserWithRepos): Completable =
        cachedDataSource.cacheUser(userWithRepos)
            .subscribeOn(Schedulers.io())

    override fun removeCachedUser(user: User): Completable =
        cachedDataSource.removeCachedUser(user)
            .subscribeOn(Schedulers.io())

    override fun getUserWithRepos(username: String): Single<UserWithRepos> =
        cachedDataSource.getSavedUserWithRepos(username)
            .subscribeOn(Schedulers.io())

    override fun hasUser(user: User): Single<Boolean> =
        cachedDataSource.hasUser(user)
            .subscribeOn(Schedulers.io())

    override fun applyLast(): Completable =
        cachedDataSource.applyLast()
            .subscribeOn(Schedulers.io())

    override fun getCachedUserList(usernameStart: String): Single<List<User>> =
        cachedDataSource.getUserList(usernameStart)
            .subscribeOn(Schedulers.io())
}