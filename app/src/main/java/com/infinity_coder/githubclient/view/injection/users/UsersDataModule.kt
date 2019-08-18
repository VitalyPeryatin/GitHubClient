package com.infinity_coder.githubclient.view.injection.users

import com.infinity_coder.githubclient.cache.base.AppDatabase
import com.infinity_coder.githubclient.cache.saved_users.dao.RepoEntityDao
import com.infinity_coder.githubclient.cache.saved_users.dao.UserEntityDao
import com.infinity_coder.githubclient.cache.saved_users.dao.UserWithReposEntityDao
import com.infinity_coder.githubclient.data.users.repository.UsersRepositoryImpl
import com.infinity_coder.githubclient.data.users.source.UsersRemoteDataSource
import com.infinity_coder.githubclient.domain.users.repository.UsersRepository
import com.infinity_coder.githubclient.remote.users.UsersRemoteDataSourceImpl
import com.infinity_coder.githubclient.remote.users.service.UsersService
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module(includes = [UsersDataModule.SearchUsersViewModelAbstractModule::class])
class UsersDataModule {
    @Provides
    fun provideGitHubService(retrofit: Retrofit): UsersService =
        retrofit.create(UsersService::class.java)

    @Provides
    fun provideUserEntityDao(appDatabase: AppDatabase): UserEntityDao =
        appDatabase.userEntityDao()

    @Provides
    fun provideRepoEntityDao(appDatabase: AppDatabase): RepoEntityDao =
        appDatabase.repoEntityDao()

    @Provides
    fun provideUserWithReposEntityDao(appDatabase: AppDatabase): UserWithReposEntityDao =
        appDatabase.userWithReposEntityDao()


    @Module
    interface SearchUsersViewModelAbstractModule {
        @Binds
        fun bindUsersRemoteDataSource(
            usersRemoteDataSource: UsersRemoteDataSourceImpl
        ): UsersRemoteDataSource

        @Binds
        fun bindUsersCachedRepository(
            usersRepository: UsersRepositoryImpl
        ): UsersRepository
    }
}