package com.infinity_coder.githubclient.data.users.remote_users.source.remote

import com.infinity_coder.githubclient.data.api.GitHubService
import com.infinity_coder.githubclient.cache.user.model.UserEntity
import io.reactivex.Single
import javax.inject.Inject

class UsersRemoteDataSourceImpl @Inject constructor(
    private val gitHubService: GitHubService
) : UsersRemoteDataSource {

    override fun getUserList(usernameStart: String): Single<List<UserEntity>> =
        gitHubService.getUserList(usernameStart)
            .map { it.items }

    override fun getUser(username: String): Single<UserEntity> =
        gitHubService.getUser(username)
}