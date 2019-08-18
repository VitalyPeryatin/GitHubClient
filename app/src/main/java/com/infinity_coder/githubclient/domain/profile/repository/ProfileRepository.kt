package com.infinity_coder.githubclient.domain.profile.repository

import com.infinity_coder.githubclient.data.base.model.Repo
import com.infinity_coder.githubclient.data.base.model.User
import com.infinity_coder.githubclient.data.base.model.UserWithRepos
import io.reactivex.Completable
import io.reactivex.Single

interface ProfileRepository {
    fun getUserList(usernameStart: String): Single<List<User>>

    fun getSavedUserList(usernameStart: String): Single<List<User>>

    fun getSavedUserWithRepos(username: String): Single<UserWithRepos>

    fun addSavedUser(userWithRepos: UserWithRepos): Completable

    fun removeSavedUser(user: User): Completable

    fun isUserSaved(user: User): Single<Boolean>

    fun applyLast(): Completable

    fun getUser(username: String): Single<User>

    fun getUserRepos(username: String): Single<List<Repo>>
}