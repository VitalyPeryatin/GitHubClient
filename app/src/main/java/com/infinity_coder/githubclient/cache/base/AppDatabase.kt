package com.infinity_coder.githubclient.cache.base

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.infinity_coder.githubclient.cache.saved_users.dao.RepoEntityDao
import com.infinity_coder.githubclient.cache.saved_users.dao.UserEntityDao
import com.infinity_coder.githubclient.cache.saved_users.dao.UserWithReposEntityDao
import com.infinity_coder.githubclient.cache.saved_users.structure.USER_DATABASE_NAME
import com.infinity_coder.githubclient.data.base.model.Repo
import com.infinity_coder.githubclient.data.base.model.User

@Database(entities = [User::class, Repo::class], version = 6)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userEntityDao(): UserEntityDao
    abstract fun repoEntityDao(): RepoEntityDao
    abstract fun userWithReposEntityDao(): UserWithReposEntityDao

    companion object {

        private var INSTANCE: AppDatabase? = null

        fun getAppDatabase(context: Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    USER_DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return INSTANCE!!
        }
    }
}