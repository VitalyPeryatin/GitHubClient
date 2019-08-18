package com.infinity_coder.githubclient.remote.users.service

import com.infinity_coder.githubclient.data.base.model.Repo
import com.infinity_coder.githubclient.data.base.model.User
import com.infinity_coder.githubclient.remote.users.model.UsersListResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UsersService {
    @GET("search/users")
    fun getUserList(@Query("q") usernameStart: String): Single<UsersListResponse>

    @GET("users/{ownerUsername}")
    fun getUser(@Path("ownerUsername") username: String): Single<User>

    @GET("users/{ownerUsername}/repos")
    fun getUserRepos(@Path("ownerUsername") username: String): Single<List<Repo>>
}