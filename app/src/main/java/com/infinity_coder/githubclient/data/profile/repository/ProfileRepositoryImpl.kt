package com.infinity_coder.githubclient.data.profile.repository

import com.infinity_coder.githubclient.data.saved_users.source.SavedUsersCachedDataSourceProxy
import com.infinity_coder.githubclient.data.base.model.Repo
import com.infinity_coder.githubclient.data.base.model.User
import com.infinity_coder.githubclient.data.base.model.UserWithRepos
import com.infinity_coder.githubclient.data.users.source.UsersRemoteDataSource
import com.infinity_coder.githubclient.domain.profile.repository.ProfileRepository
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val cachedDataSource: SavedUsersCachedDataSourceProxy,
    private val remoteDataSource: UsersRemoteDataSource
) : ProfileRepository {

    override fun addSavedUser(userWithRepos: UserWithRepos): Completable =
        cachedDataSource.cacheUser(userWithRepos)
            .subscribeOn(Schedulers.io())

    override fun removeSavedUser(user: User): Completable =
        cachedDataSource.removeCachedUser(user)
            .subscribeOn(Schedulers.io())

    override fun getUserList(usernameStart: String): Single<List<User>> =
        cachedDataSource.getUserList(usernameStart)
            .subscribeOn(Schedulers.io())

    override fun getSavedUserWithRepos(username: String): Single<UserWithRepos> =
        cachedDataSource.getSavedUserWithRepos(username)
            .subscribeOn(Schedulers.io())

    override fun isUserSaved(user: User): Single<Boolean> =
        cachedDataSource.hasUser(user)
            .subscribeOn(Schedulers.io())

    override fun applyLast(): Completable =
        cachedDataSource.applyLast()
            .subscribeOn(Schedulers.io())

    override fun getSavedUserList(usernameStart: String): Single<List<User>> =
        remoteDataSource.getUserList(usernameStart)
            .subscribeOn(Schedulers.io())

    override fun getUser(username: String): Single<User> =
        remoteDataSource.getUser(username)
            .subscribeOn(Schedulers.io())

    override fun getUserRepos(username: String): Single<List<Repo>> =
        remoteDataSource.getUserRepos(username)
            .subscribeOn(Schedulers.io())
}