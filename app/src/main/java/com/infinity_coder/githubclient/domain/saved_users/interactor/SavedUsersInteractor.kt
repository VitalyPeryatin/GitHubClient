package com.infinity_coder.githubclient.domain.saved_users.interactor

import com.infinity_coder.githubclient.domain.saved_users.repository.SavedUsersRepository
import com.infinity_coder.githubclient.data.base.model.User
import com.infinity_coder.githubclient.data.base.model.UserWithRepos
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class SavedUsersInteractor @Inject constructor(
    private val cachedRepository: SavedUsersRepository
) {
    fun getCachedUserList(usernameBegin: String): Single<List<User>> =
        cachedRepository.getCachedUserList(usernameBegin)

    fun getUser(username: String): Single<UserWithRepos> =
        cachedRepository.getUserWithRepos(username)

    fun addCachedUser(userWithRepos: UserWithRepos): Completable =
        cachedRepository.addCachedUser(userWithRepos)

    fun removeCachedUser(user: User): Completable =
        cachedRepository.removeCachedUser(user)

    fun hasCachedUser(user: User): Single<Boolean> =
        cachedRepository.hasUser(user)

    fun applyLast(): Completable =
        cachedRepository.applyLast()
}