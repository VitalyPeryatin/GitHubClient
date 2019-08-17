package com.infinity_coder.githubclient.data.users.remote_users.repository

import com.infinity_coder.githubclient.cache.user.model.UserEntity
import com.infinity_coder.githubclient.data.users.remote_users.source.remote.UsersRemoteDataSource
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class UsersRemoteRepositoryImpl @Inject constructor(
    private val remoteDataSource: UsersRemoteDataSource
) : UsersRemoteRepository {

    override fun getUserList(usernameStart: String): Single<List<UserEntity>> =
        remoteDataSource.getUserList(usernameStart)
            .subscribeOn(Schedulers.io())

    override fun getUser(username: String): Single<UserEntity> =
        remoteDataSource.getUser(username)
            .subscribeOn(Schedulers.io())
}