package com.infinity_coder.githubclient.view.injection.profile

import com.infinity_coder.githubclient.cache.base.AppDatabase
import com.infinity_coder.githubclient.cache.saved_users.SavedUsersCacheDataSourceImpl
import com.infinity_coder.githubclient.cache.saved_users.SavedUsersCacheDataSourceProxyImpl
import com.infinity_coder.githubclient.cache.saved_users.dao.RepoEntityDao
import com.infinity_coder.githubclient.cache.saved_users.dao.UserEntityDao
import com.infinity_coder.githubclient.cache.saved_users.dao.UserWithReposEntityDao
import com.infinity_coder.githubclient.data.profile.repository.ProfileRepositoryImpl
import com.infinity_coder.githubclient.data.saved_users.source.SavedUsersCacheDataSource
import com.infinity_coder.githubclient.data.saved_users.source.SavedUsersCachedDataSourceProxy
import com.infinity_coder.githubclient.data.users.source.UsersRemoteDataSource
import com.infinity_coder.githubclient.domain.profile.repository.ProfileRepository
import com.infinity_coder.githubclient.remote.users.UsersRemoteDataSourceImpl
import com.infinity_coder.githubclient.remote.users.service.UsersService
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module(includes = [ProfileDataModule.ProfileViewModelAbstractModule::class])
class ProfileDataModule {

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
    interface ProfileViewModelAbstractModule {
        @Binds
        fun bindUsersCachedDataSource(
            usersCachedDataSource: SavedUsersCacheDataSourceImpl
        ): SavedUsersCacheDataSource

        @Binds
        fun bindSavedUsersCachedDataSourceProxy(
            savedUsersCacheDataSourceImpl: SavedUsersCacheDataSourceProxyImpl
        ): SavedUsersCachedDataSourceProxy

        @Binds
        fun bindUsersRemoteDataSource(
            usersRemoteDataSource: UsersRemoteDataSourceImpl
        ): UsersRemoteDataSource

        @Binds
        fun bindUsersCachedRepository(
            usersRepository: ProfileRepositoryImpl
        ): ProfileRepository
    }
}