package com.infinity_coder.githubclient.view.injection.profile

import com.infinity_coder.githubclient.view.injection.app.AppComponent
import com.infinity_coder.githubclient.view.profile.ui.ProfileFragment
import dagger.Component
import retrofit2.Retrofit

@ProfileScope
@Component(modules = [ProfileModule::class], dependencies = [AppComponent::class])
interface ProfileComponent {
    val retrofit: Retrofit
    fun inject(fragment: ProfileFragment)
}