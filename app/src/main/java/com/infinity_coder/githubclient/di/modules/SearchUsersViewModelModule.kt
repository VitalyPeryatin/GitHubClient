package com.infinity_coder.githubclient.di.modules

import com.infinity_coder.githubclient.data.api.GitHubService
import com.infinity_coder.githubclient.data.users.remote_users.repository.UsersRemoteRepository
import com.infinity_coder.githubclient.data.users.remote_users.repository.UsersRemoteRepositoryImpl
import com.infinity_coder.githubclient.data.users.remote_users.source.remote.UsersRemoteDataSource
import com.infinity_coder.githubclient.data.users.remote_users.source.remote.UsersRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module(includes = [SearchUsersViewModelModule.SearchUsersViewModelAbstractModule::class])
class SearchUsersViewModelModule {
    @Provides
    fun provideGitHubService(retrofit: Retrofit): GitHubService =
        retrofit.create(GitHubService::class.java)

    @Module
    interface SearchUsersViewModelAbstractModule {
        @Binds
        fun bindUsersRepository(usersRepository: UsersRemoteRepositoryImpl): UsersRemoteRepository

        @Binds
        fun bindUsersRemoteDataSource(usersRemoteDataSource: UsersRemoteDataSourceImpl): UsersRemoteDataSource
    }
}