package com.infinity_coder.githubclient.view.injection.users

import com.infinity_coder.githubclient.view.injection.app.AppComponent
import com.infinity_coder.githubclient.view.users.ui.UsersFragment
import dagger.Component
import retrofit2.Retrofit

@UsersScope
@Component(modules = [UsersDataModule::class], dependencies = [AppComponent::class])
interface UsersComponent {
    val retrofit: Retrofit
    fun inject(fragment: UsersFragment)
}