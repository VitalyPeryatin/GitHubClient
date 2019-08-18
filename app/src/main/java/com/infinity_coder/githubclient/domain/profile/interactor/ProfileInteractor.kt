package com.infinity_coder.githubclient.domain.profile.interactor

import com.infinity_coder.githubclient.data.base.model.Repo
import com.infinity_coder.githubclient.data.base.model.User
import com.infinity_coder.githubclient.data.base.model.UserWithRepos
import com.infinity_coder.githubclient.domain.profile.repository.ProfileRepository
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class ProfileInteractor @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    fun getUser(username: String): Single<User> =
        profileRepository.getUser(username)

    fun isUserSaved(userEntity: User): Single<Boolean> =
        profileRepository.isUserSaved(userEntity)

    fun getUserRepos(username: String): Single<List<Repo>> =
        profileRepository.getUserRepos(username)

    fun getSavedUserWithRepos(username: String): Single<UserWithRepos> =
        profileRepository.getSavedUserWithRepos(username)

    fun removeSavedUser(userEntity: User): Completable =
        profileRepository.removeSavedUser(userEntity)

    fun addSavedUser(userWithReposEntity: UserWithRepos): Completable =
        profileRepository.addSavedUser(userWithReposEntity)

    fun applyLast(): Completable =
        profileRepository.applyLast()
}