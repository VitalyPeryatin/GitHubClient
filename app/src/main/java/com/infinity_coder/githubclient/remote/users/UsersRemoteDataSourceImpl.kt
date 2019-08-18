package com.infinity_coder.githubclient.remote.users

import com.infinity_coder.githubclient.data.base.model.Repo
import com.infinity_coder.githubclient.data.base.model.User
import com.infinity_coder.githubclient.data.users.source.UsersRemoteDataSource
import com.infinity_coder.githubclient.remote.users.service.UsersService
import io.reactivex.Single
import javax.inject.Inject

class UsersRemoteDataSourceImpl @Inject constructor(
    private val gitHubService: UsersService
) : UsersRemoteDataSource {
    override fun getUserList(usernameStart: String): Single<List<User>> =
        gitHubService.getUserList(usernameStart)
            .map { it.items }

    override fun getUser(username: String): Single<User> =
        gitHubService.getUser(username)

    override fun getUserRepos(username: String): Single<List<Repo>> =
        gitHubService.getUserRepos(username)
}