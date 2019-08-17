package com.infinity_coder.githubclient.di.modules

import com.infinity_coder.githubclient.cache.base.AppDatabase
import com.infinity_coder.githubclient.cache.user.dao.UserEntityDao
import com.infinity_coder.githubclient.data.users.cached_users.repositories.user.UsersCachedRepository
import com.infinity_coder.githubclient.data.users.cached_users.repositories.user.UsersCachedRepositoryImpl
import com.infinity_coder.githubclient.data.users.cached_users.source.user.UsersCachedDataSource
import com.infinity_coder.githubclient.cache.user.UsersCachedDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module(includes = [CachedUsersViewModelModule.CachedUsersViewModelAbstractModule::class])
class CachedUsersViewModelModule {

    @Provides
    fun provideUserDao(appDatabase: AppDatabase): UserEntityDao =
        appDatabase.userDao()

    @Module
    interface CachedUsersViewModelAbstractModule {
        @Binds
        fun bindUsersRepository(usersRepository: UsersCachedRepositoryImpl): UsersCachedRepository

        @Binds
        fun bindUsersRemoteDataSource(usersRemoteDataSource: UsersCachedDataSourceImpl): UsersCachedDataSource
    }
}