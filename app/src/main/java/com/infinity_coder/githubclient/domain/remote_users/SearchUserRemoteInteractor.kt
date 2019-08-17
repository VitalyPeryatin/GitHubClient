package com.infinity_coder.githubclient.domain.remote_users

import com.infinity_coder.githubclient.cache.user.model.UserEntity
import com.infinity_coder.githubclient.data.users.remote_users.repository.UsersRemoteRepository
import io.reactivex.Single
import javax.inject.Inject

class SearchUserRemoteInteractor @Inject constructor(
    private val searchUsersRepository: UsersRemoteRepository
) {
    fun getUserList(usernameStart: String): Single<List<UserEntity>> =
        searchUsersRepository.getUserList(usernameStart)

    fun getUser(username: String): Single<UserEntity> =
        searchUsersRepository.getUser(username)
}