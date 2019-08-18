package com.infinity_coder.githubclient.domain.saved_users.repository

import com.infinity_coder.githubclient.data.base.model.User
import com.infinity_coder.githubclient.data.base.model.UserWithRepos
import io.reactivex.Completable
import io.reactivex.Single

interface SavedUsersRepository {
    fun addCachedUser(userWithRepos: UserWithRepos): Completable

    fun removeCachedUser(user: User): Completable

    fun getUserWithRepos(username: String): Single<UserWithRepos>

    fun hasUser(user: User): Single<Boolean>

    fun applyLast(): Completable

    fun getCachedUserList(usernameStart: String): Single<List<User>>
}