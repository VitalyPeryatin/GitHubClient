package com.infinity_coder.githubclient.view.injection.app

import android.content.Context
import com.infinity_coder.githubclient.cache.base.AppDatabase
import dagger.BindsInstance
import dagger.Component
import retrofit2.Retrofit

@AppScope
@Component(modules = [AppModule::class])
interface AppComponent {
    val retrofit: Retrofit
    val appDatabase: AppDatabase
    val context: Context

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun appContext(appContext: Context): Builder
        fun build(): AppComponent
    }
}