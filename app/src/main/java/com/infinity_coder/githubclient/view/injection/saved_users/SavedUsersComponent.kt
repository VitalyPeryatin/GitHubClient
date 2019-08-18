package com.infinity_coder.githubclient.view.injection.saved_users

import com.infinity_coder.githubclient.cache.base.AppDatabase
import com.infinity_coder.githubclient.view.injection.app.AppComponent
import com.infinity_coder.githubclient.view.saved_users.ui.SavedUsersFragment
import dagger.Component

@SavedUsersScope
@Component(modules = [SavedUsersModule::class], dependencies = [AppComponent::class])
interface SavedUsersComponent {
    val appDatabase: AppDatabase
    fun inject(fragment: SavedUsersFragment)
}