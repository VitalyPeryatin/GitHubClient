package com.infinity_coder.githubclient.domain.user_repos

import com.infinity_coder.githubclient.cache.user.model.RepoEntity
import com.infinity_coder.githubclient.data.users.cached_users.repositories.repos.UserReposRepository
import io.reactivex.Single
import javax.inject.Inject

class UserReposInteractor @Inject constructor(
    private val userReposRepository: UserReposRepository
) {
    fun getUserRepos(username: String): Single<List<RepoEntity>> =
            userReposRepository.getUserRepos(username)
}