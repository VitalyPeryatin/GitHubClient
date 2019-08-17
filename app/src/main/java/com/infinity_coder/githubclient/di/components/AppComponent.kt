package com.infinity_coder.githubclient.di.components

import android.content.Context
import com.infinity_coder.githubclient.cache.base.AppDatabase
import com.infinity_coder.githubclient.di.modules.AppModule
import dagger.Component
import retrofit2.Retrofit
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    val retrofit: Retrofit
    val appDatabase: AppDatabase
    val appContext: Context
}