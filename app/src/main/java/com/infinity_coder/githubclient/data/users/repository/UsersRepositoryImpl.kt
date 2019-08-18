package com.infinity_coder.githubclient.data.users.repository

import com.infinity_coder.githubclient.data.base.model.Repo
import com.infinity_coder.githubclient.data.base.model.User
import com.infinity_coder.githubclient.data.users.source.UsersRemoteDataSource
import com.infinity_coder.githubclient.domain.users.repository.UsersRepository
import io.reactivex.Single
import javax.inject.Inject

class UsersRepositoryImpl @Inject constructor(
    private val remoteDataSource: UsersRemoteDataSource
) : UsersRepository {

    override fun getUserList(usernameStart: String): Single<List<User>> =
        remoteDataSource.getUserList(usernameStart)

    override fun getUser(username: String): Single<User> =
        remoteDataSource.getUser(username)

    override fun getUserRepos(username: String): Single<List<Repo>> =
        remoteDataSource.getUserRepos(username)

}