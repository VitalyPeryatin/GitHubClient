package com.infinity_coder.githubclient.view.injection.saved_users

import com.infinity_coder.githubclient.cache.base.AppDatabase
import com.infinity_coder.githubclient.cache.saved_users.SavedUsersCacheDataSourceImpl
import com.infinity_coder.githubclient.cache.saved_users.SavedUsersCacheDataSourceProxyImpl
import com.infinity_coder.githubclient.cache.saved_users.dao.RepoEntityDao
import com.infinity_coder.githubclient.cache.saved_users.dao.UserEntityDao
import com.infinity_coder.githubclient.cache.saved_users.dao.UserWithReposEntityDao
import com.infinity_coder.githubclient.data.saved_users.repository.SavedUsersRepositoryImpl
import com.infinity_coder.githubclient.data.saved_users.source.SavedUsersCacheDataSource
import com.infinity_coder.githubclient.data.saved_users.source.SavedUsersCachedDataSourceProxy
import com.infinity_coder.githubclient.domain.saved_users.repository.SavedUsersRepository
import com.infinity_coder.githubclient.remote.users.service.UsersService
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module(includes = [SavedUsersDataModule.CachedUsersViewModelAbstractModule::class])
class SavedUsersDataModule {

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
    interface CachedUsersViewModelAbstractModule {
        @Binds
        fun bindUsersCachedDataSource(
            usersCachedDataSource: SavedUsersCacheDataSourceImpl
        ): SavedUsersCacheDataSource

        @Binds
        fun bindSavedUsersCachedDataSourceProxy(
            savedUsersCacheDataSourceImpl: SavedUsersCacheDataSourceProxyImpl
        ): SavedUsersCachedDataSourceProxy

        @Binds
        fun bindUsersCachedRepository(
            usersRepository: SavedUsersRepositoryImpl
        ): SavedUsersRepository
    }
}