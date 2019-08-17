package com.infinity_coder.githubclient.data.users.cached_users.repositories.user

import com.infinity_coder.githubclient.cache.user.model.UserEntity
import com.infinity_coder.githubclient.cache.user.model.UserWithReposEntity
import io.reactivex.Completable
import io.reactivex.Single

interface UsersCachedRepository {
    fun getUserList(usernameStart: String): Single<List<UserEntity>>

    fun getUserWithRepos(username: String): Single<UserWithReposEntity>

    fun addCachedUser(userWithRepos: UserWithReposEntity): Completable

    fun removeCachedUser(user: UserEntity): Completable

    fun hasUser(user: UserEntity): Single<Boolean>

    fun applyLast(): Completable
}