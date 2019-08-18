package com.infinity_coder.githubclient.cache.base

import androidx.room.Database
import androidx.room.RoomDatabase
import com.infinity_coder.githubclient.cache.saved_users.dao.RepoEntityDao
import com.infinity_coder.githubclient.cache.saved_users.dao.UserEntityDao
import com.infinity_coder.githubclient.cache.saved_users.dao.UserWithReposEntityDao
import com.infinity_coder.githubclient.data.base.model.Repo
import com.infinity_coder.githubclient.data.base.model.User

@Database(entities = [User::class, Repo::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userEntityDao(): UserEntityDao
    abstract fun repoEntityDao(): RepoEntityDao
    abstract fun userWithReposEntityDao(): UserWithReposEntityDao
}