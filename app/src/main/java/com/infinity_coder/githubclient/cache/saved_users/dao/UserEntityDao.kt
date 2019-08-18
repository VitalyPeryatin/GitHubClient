package com.infinity_coder.githubclient.cache.saved_users.dao

import androidx.room.*
import com.infinity_coder.githubclient.data.base.model.User
import io.reactivex.Single

@Dao
interface UserEntityDao {
    @Query("SELECT COUNT(*) FROM User WHERE login=:username")
    fun usersCount(username: String) : Single<Int>

    @Query("SELECT * FROM User WHERE login LIKE :usernameBegin || '%' ")
    fun getUsersByUsernameBegin(usernameBegin: String): Single<List<User>>

    @Transaction
    @Query("SELECT * FROM User WHERE login=:username LIMIT 1")
    fun getUserByUsername(username: String): Single<User>
}