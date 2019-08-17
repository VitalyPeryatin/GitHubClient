package com.infinity_coder.githubclient.domain.cached_users

import com.infinity_coder.githubclient.cache.user.model.UserEntity
import com.infinity_coder.githubclient.cache.user.model.UserWithReposEntity
import com.infinity_coder.githubclient.data.users.cached_users.repositories.user.UsersCachedRepository
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class UserCachedInteractor @Inject constructor(
    private val cachedRepository: UsersCachedRepository
) {
    fun getUserList(usernameBegin: String): Single<List<UserEntity>> =
        cachedRepository.getUserList(usernameBegin)

    fun getUser(username: String): Single<UserWithReposEntity> =
        cachedRepository.getUserWithRepos(username)

    fun addCachedUser(userWithRepos: UserWithReposEntity): Completable =
        cachedRepository.addCachedUser(userWithRepos)

    fun removeCachedUser(user: UserEntity): Completable =
        cachedRepository.removeCachedUser(user)

    fun hasCachedUser(user: UserEntity): Single<Boolean> =
        cachedRepository.hasUser(user)

    fun applyLast(): Completable =
        cachedRepository.applyLast()
}