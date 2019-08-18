package com.infinity_coder.githubclient.view.injection.app

import android.content.Context
import androidx.room.Room
import com.infinity_coder.githubclient.cache.base.AppDatabase
import com.infinity_coder.githubclient.cache.saved_users.structure.USER_DATABASE_NAME
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
class AppModule {
    @Provides
    @AppScope
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

    @Provides
    @AppScope
    fun provideAppDatabase(context: Context): AppDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            USER_DATABASE_NAME
        ).fallbackToDestructiveMigration()
            .build()

    private companion object {
        const val API_URL = "https://api.github.com/"
    }
}