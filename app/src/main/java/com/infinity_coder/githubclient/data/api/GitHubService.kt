package com.infinity_coder.githubclient.data.api

import com.infinity_coder.githubclient.cache.user.model.RepoEntity
import com.infinity_coder.githubclient.cache.user.model.UserEntity
import com.infinity_coder.githubclient.data.users.remote_users.model.UserListResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubService {
    @GET("search/users")
    fun getUserList(@Query("q") usernameStart: String): Single<UserListResponse>

    @GET("users/{ownerUsername}")
    fun getUser(@Path("ownerUsername") username: String): Single<UserEntity>

    @GET("users/{ownerUsername}/repos")
    fun getUserRepos(@Path("ownerUsername") username: String): Single<List<RepoEntity>>
}