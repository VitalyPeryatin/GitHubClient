package com.infinity_coder.githubclient.data.saved_users.source

import com.infinity_coder.githubclient.data.base.model.User
import com.infinity_coder.githubclient.data.base.model.UserWithRepos
import io.reactivex.Completable
import io.reactivex.Single

interface SavedUsersCacheDataSource {
    fun getUserList(usernameStart: String): Single<List<User>>

    fun getSavedUserWithRepos(username: String): Single<UserWithRepos>

    fun cacheUser(userWithRepos: UserWithRepos): Completable

    fun removeCachedUser(user: User): Completable

    fun hasUser(user: User): Single<Boolean>
}