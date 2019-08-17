package com.infinity_coder.githubclient.data.users.remote_users.source.remote

import com.infinity_coder.githubclient.cache.user.model.UserEntity
import io.reactivex.Single

interface UsersRemoteDataSource {
    fun getUserList(usernameStart: String): Single<List<UserEntity>>

    fun getUser(username: String): Single<UserEntity>
}