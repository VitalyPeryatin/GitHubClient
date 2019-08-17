package com.infinity_coder.githubclient.data.users.cached_users.source.repos

import com.infinity_coder.githubclient.cache.user.model.RepoEntity
import io.reactivex.Single

interface UserReposRemoteDataSource {
    fun getUserRepos(username: String): Single<List<RepoEntity>>
}