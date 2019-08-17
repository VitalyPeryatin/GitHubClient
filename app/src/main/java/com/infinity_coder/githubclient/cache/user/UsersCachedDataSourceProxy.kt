package com.infinity_coder.githubclient.cache.user

import com.infinity_coder.githubclient.cache.user.model.UserEntity
import com.infinity_coder.githubclient.cache.user.model.UserWithReposEntity
import com.infinity_coder.githubclient.data.users.cached_users.source.user.UsersCachedDataSource
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class UsersCachedDataSourceProxy @Inject constructor(
    private val usersCachedDataSource: UsersCachedDataSource
) : UsersCachedDataSource by usersCachedDataSource {

    // Если 'isCached == null' - изменний над сохранением пользователя не совершали
    private var isCached: Boolean? = null

    private var pendingCommand: Completable? = null

    override fun cacheUser(userWithRepos: UserWithReposEntity): Completable =
        Completable.create { emitter ->
            isCached = true
            pendingCommand = usersCachedDataSource.cacheUser(userWithRepos)
            emitter.onComplete()
        }

    override fun removeCachedUser(user: UserEntity): Completable =
        Completable.create { emitter ->
            isCached = false
            pendingCommand = usersCachedDataSource.removeCachedUser(user)
            emitter.onComplete()
        }

    override fun hasUser(user: UserEntity): Single<Boolean> =
        if (isCached == null) {
            usersCachedDataSource.hasUser(user)
        } else {
            Single.just(isCached)
        }

    fun applyLast(): Completable =
        if (isCached != null) {
            pendingCommand!!
        } else {
            Completable.complete()
        }
}