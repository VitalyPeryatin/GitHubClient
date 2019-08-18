package com.infinity_coder.githubclient.domain.saved_users.interactor

import com.infinity_coder.githubclient.data.base.model.User
import com.infinity_coder.githubclient.data.base.model.UserWithRepos
import com.infinity_coder.githubclient.domain.saved_users.repository.SavedUsersRepository
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
}