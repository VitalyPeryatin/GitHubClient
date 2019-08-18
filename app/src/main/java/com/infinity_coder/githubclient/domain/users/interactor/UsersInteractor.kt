package com.infinity_coder.githubclient.domain.users.interactor

import com.infinity_coder.githubclient.data.base.model.Repo
import com.infinity_coder.githubclient.data.base.model.User
import com.infinity_coder.githubclient.domain.users.repository.UsersRepository
import io.reactivex.Single
import javax.inject.Inject

class UsersInteractor @Inject constructor(
    private val searchUsersRepository: UsersRepository
) {
    fun getUserList(usernameStart: String): Single<List<User>> =
        searchUsersRepository.getUserList(usernameStart)

    fun getUser(username: String): Single<User> =
        searchUsersRepository.getUser(username)

    fun getUserRepos(username: String): Single<List<Repo>> =
        searchUsersRepository.getUserRepos(username)
}