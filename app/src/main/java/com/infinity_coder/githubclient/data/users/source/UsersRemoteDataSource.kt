package com.infinity_coder.githubclient.data.users.source

import com.infinity_coder.githubclient.data.base.model.Repo
import com.infinity_coder.githubclient.data.base.model.User
import io.reactivex.Single

interface UsersRemoteDataSource {
    fun getUserList(usernameStart: String): Single<List<User>>

    fun getUser(username: String): Single<User>

    fun getUserRepos(username: String): Single<List<Repo>>
}