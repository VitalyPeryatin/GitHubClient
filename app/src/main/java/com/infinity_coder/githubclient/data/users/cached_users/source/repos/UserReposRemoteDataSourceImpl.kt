package com.infinity_coder.githubclient.data.users.cached_users.source.repos

import com.infinity_coder.githubclient.cache.user.model.RepoEntity
import com.infinity_coder.githubclient.data.api.GitHubService
import io.reactivex.Single
import javax.inject.Inject

class UserReposRemoteDataSourceImpl @Inject constructor(
    private val gitHubService: GitHubService
) : UserReposRemoteDataSource {

    override fun getUserRepos(username: String): Single<List<RepoEntity>> =
        gitHubService.getUserRepos(username)
}