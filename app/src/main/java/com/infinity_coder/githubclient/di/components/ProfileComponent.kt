package com.infinity_coder.githubclient.di.components

import android.content.Context
import com.infinity_coder.githubclient.di.modules.ProfileViewModelModule
import com.infinity_coder.githubclient.di.scopes.ProfileScope
import com.infinity_coder.githubclient.view.profile.ProfileFragment
import dagger.Component
import retrofit2.Retrofit

@ProfileScope
@Component(modules = [ProfileViewModelModule::class], dependencies = [AppComponent::class])
interface ProfileComponent {
    val retrofit: Retrofit
    val appContext: Context
    fun inject(fragment: ProfileFragment)
}