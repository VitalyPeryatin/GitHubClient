package com.infinity_coder.githubclient.di.components

import android.content.Context
import com.infinity_coder.githubclient.cache.base.AppDatabase
import com.infinity_coder.githubclient.di.modules.CachedUsersViewModelModule
import com.infinity_coder.githubclient.di.scopes.FavouriteUsersScope
import com.infinity_coder.githubclient.view.cache_users.CacheUsersFragment
import dagger.Component

@FavouriteUsersScope
@Component(modules = [CachedUsersViewModelModule::class], dependencies = [AppComponent::class])
interface CachedUsersComponent {
    val appDatabase: AppDatabase
    val appContext: Context
    fun inject(fragment: CacheUsersFragment)
}