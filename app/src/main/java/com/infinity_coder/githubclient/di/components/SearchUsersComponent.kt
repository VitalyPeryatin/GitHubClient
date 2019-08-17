package com.infinity_coder.githubclient.di.components

import com.infinity_coder.githubclient.di.modules.SearchUsersViewModelModule
import com.infinity_coder.githubclient.di.scopes.SearchUsersScope
import com.infinity_coder.githubclient.view.remote_users.SearchUsersFragment
import dagger.Component
import retrofit2.Retrofit

@SearchUsersScope
@Component(modules = [SearchUsersViewModelModule::class], dependencies = [AppComponent::class])
interface SearchUsersComponent {
    val retrofit: Retrofit
    fun inject(fragment: SearchUsersFragment)
}