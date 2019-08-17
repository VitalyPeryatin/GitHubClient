package com.infinity_coder.githubclient.cache.base

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.infinity_coder.githubclient.cache.user.model.RepoEntity
import com.infinity_coder.githubclient.cache.user.dao.UserEntityDao
import com.infinity_coder.githubclient.cache.user.model.UserEntity

@Database(entities = [UserEntity::class, RepoEntity::class], version = 6)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserEntityDao

    companion object {

        private var INSTANCE: AppDatabase? = null

        fun getAppDatabase(context: Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "UserDatabase")
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return INSTANCE!!
        }
    }
}