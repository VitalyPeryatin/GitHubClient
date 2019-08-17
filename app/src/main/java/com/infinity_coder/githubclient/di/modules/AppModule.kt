package com.infinity_coder.githubclient.di.modules

import android.content.Context
import com.infinity_coder.githubclient.cache.base.AppDatabase
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class AppModule(
    private val context: Context
) {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideAppDatabase(): AppDatabase =
        AppDatabase.getAppDatabase(context)

    @Provides
    @Singleton
    fun provideContext(): Context =
        context

    companion object {
        private const val API_URL = "https://api.github.com/"
    }
}