package com.infinity_coder.githubclient.di.modules

import com.infinity_coder.githubclient.cache.base.AppDatabase
import com.infinity_coder.githubclient.data.api.GitHubService
import com.infinity_coder.githubclient.data.users.cached_users.repositories.repos.UserReposRepository
import com.infinity_coder.githubclient.data.users.cached_users.repositories.repos.UserReposRepositoryImpl
import com.infinity_coder.githubclient.data.users.cached_users.source.repos.UserReposRemoteDataSource
import com.infinity_coder.githubclient.data.users.cached_users.source.repos.UserReposRemoteDataSourceImpl
import com.infinity_coder.githubclient.cache.user.dao.UserEntityDao
import com.infinity_coder.githubclient.data.users.cached_users.repositories.user.UsersCachedRepository
import com.infinity_coder.githubclient.data.users.cached_users.repositories.user.UsersCachedRepositoryImpl
import com.infinity_coder.githubclient.data.users.cached_users.source.user.UsersCachedDataSource
import com.infinity_coder.githubclient.cache.user.UsersCachedDataSourceImpl
import com.infinity_coder.githubclient.data.users.remote_users.repository.UsersRemoteRepository
import com.infinity_coder.githubclient.data.users.remote_users.repository.UsersRemoteRepositoryImpl
import com.infinity_coder.githubclient.data.users.remote_users.source.remote.UsersRemoteDataSource
import com.infinity_coder.githubclient.data.users.remote_users.source.remote.UsersRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module(includes = [ProfileViewModelModule.ProfileViewModelAbstractModule::class])
class ProfileViewModelModule {

    @Provides
    fun provideGitHubService(retrofit: Retrofit): GitHubService =
        retrofit.create(GitHubService::class.java)

    @Provides
    fun provideUserDao(appDatabase: AppDatabase): UserEntityDao =
        appDatabase.userDao()

    @Module
    interface ProfileViewModelAbstractModule {
        @Binds
        fun bindUserReposRepository(
            usersReposRepository: UserReposRepositoryImpl
        ): UserReposRepository

        @Binds
        fun bindUserReposRemoteDataSource(
            userReposRemoteDataSource: UserReposRemoteDataSourceImpl
        ): UserReposRemoteDataSource

        @Binds
        fun bindUsersRemoteRepository(
            usersRepository: UsersRemoteRepositoryImpl
        ): UsersRemoteRepository

        @Binds
        fun bindUsersRemoteDataSource(
            usersRemoteDataSource: UsersRemoteDataSourceImpl
        ): UsersRemoteDataSource

        @Binds
        fun bindUsersCachedRepository(
            usersRepository: UsersCachedRepositoryImpl
        ): UsersCachedRepository

        @Binds
        fun bindUsersCachedDataSource(
            usersCachedDataSource: UsersCachedDataSourceImpl
        ): UsersCachedDataSource
    }
}