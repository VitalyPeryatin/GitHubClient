package com.infinity_coder.githubclient.data.users.cached_users.repositories.repos

import com.infinity_coder.githubclient.cache.user.model.RepoEntity
import com.infinity_coder.githubclient.data.users.cached_users.source.repos.UserReposRemoteDataSource
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class UserReposRepositoryImpl @Inject constructor(
    private val userRemoteDataSource: UserReposRemoteDataSource
) : UserReposRepository {

    override fun getUserRepos(username: String): Single<List<RepoEntity>> =
        userRemoteDataSource.getUserRepos(username)
            .subscribeOn(Schedulers.io())
}