package com.infinity_coder.githubclient.cache.saved_users

import com.infinity_coder.githubclient.data.saved_users.source.SavedUsersCacheDataSource
import com.infinity_coder.githubclient.data.saved_users.source.SavedUsersCachedDataSourceProxy
import com.infinity_coder.githubclient.data.base.model.User
import com.infinity_coder.githubclient.data.base.model.UserWithRepos
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class SavedUsersCacheDataSourceProxyImpl @Inject constructor(
    private val usersCachedDataSource: SavedUsersCacheDataSource
) : SavedUsersCachedDataSourceProxy {

    // Если 'isCached == null' - изменний над сохранением пользователя не совершали
    private var isCached: Boolean? = null

    private var pendingCommand: Completable? = null

    override fun cacheUser(userWithRepos: UserWithRepos): Completable =
        Completable.create { emitter ->
            isCached = true
            pendingCommand = usersCachedDataSource.cacheUser(userWithRepos)
            emitter.onComplete()
        }


    override fun removeCachedUser(user: User): Completable =
        Completable.create { emitter ->
            isCached = false
            pendingCommand = usersCachedDataSource.removeCachedUser(user)
            emitter.onComplete()
        }

    override fun hasUser(user: User): Single<Boolean> =
        if (isCached == null) {
            usersCachedDataSource.hasUser(user)
        } else {
            Single.just(isCached)
        }

    override fun getUserList(usernameStart: String): Single<List<User>> =
        usersCachedDataSource.getUserList(usernameStart)

    override fun getSavedUserWithRepos(username: String): Single<UserWithRepos> =
        usersCachedDataSource.getSavedUserWithRepos(username)


    override fun applyLast(): Completable =
        if (isCached != null) {
            pendingCommand!!
        } else {
            Completable.complete()
        }
}