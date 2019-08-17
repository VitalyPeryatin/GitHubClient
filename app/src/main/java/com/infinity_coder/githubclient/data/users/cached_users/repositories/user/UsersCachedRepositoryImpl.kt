package com.infinity_coder.githubclient.data.users.cached_users.repositories.user

import com.infinity_coder.githubclient.cache.user.model.UserEntity
import com.infinity_coder.githubclient.cache.user.model.UserWithReposEntity
import com.infinity_coder.githubclient.cache.user.UsersCachedDataSourceProxy
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class UsersCachedRepositoryImpl @Inject constructor(
    private val cachedDataSourceProxy: UsersCachedDataSourceProxy
) : UsersCachedRepository {

    override fun addCachedUser(userWithRepos: UserWithReposEntity): Completable =
        cachedDataSourceProxy.cacheUser(userWithRepos)
            .subscribeOn(Schedulers.io())

    override fun removeCachedUser(user: UserEntity): Completable =
        cachedDataSourceProxy.removeCachedUser(user)
            .subscribeOn(Schedulers.io())

    override fun getUserList(usernameStart: String): Single<List<UserEntity>> =
        cachedDataSourceProxy.getUserList(usernameStart)
            .subscribeOn(Schedulers.io())

    override fun getUserWithRepos(username: String): Single<UserWithReposEntity> =
        cachedDataSourceProxy.getUserWithRepos(username)
            .subscribeOn(Schedulers.io())

    override fun hasUser(user: UserEntity): Single<Boolean> =
        cachedDataSourceProxy.hasUser(user)
            .subscribeOn(Schedulers.io())

    override fun applyLast(): Completable =
        cachedDataSourceProxy.applyLast()
            .subscribeOn(Schedulers.io())
}