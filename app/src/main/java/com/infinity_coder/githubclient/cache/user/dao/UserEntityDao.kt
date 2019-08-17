package com.infinity_coder.githubclient.cache.user.dao

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.infinity_coder.githubclient.cache.user.model.RepoEntity
import com.infinity_coder.githubclient.cache.user.model.UserEntity
import com.infinity_coder.githubclient.cache.user.model.UserWithReposEntity
import io.reactivex.Single


@Dao
interface UserEntityDao {

    @Query("SELECT * FROM User")
    fun getAllUsers(): Single<List<UserEntity>>

    @Query("SELECT * FROM User WHERE login LIKE :usernameBegin || '%' ")
    fun getUsersByUsernameBegin(usernameBegin: String): Single<List<UserEntity>>

    @Transaction
    @Query("SELECT * FROM User WHERE login=:username LIMIT 1")
    fun getUserByUsername(username: String): Single<UserEntity>

    @Query("SELECT * FROM Repo WHERE ownerUsername=:ownerUsername")
    fun getAllReposByOwnerUsername(ownerUsername: String): Single<List<RepoEntity>>

    @Insert(onConflict = REPLACE)
    fun insertUser(user: UserEntity)

    @Delete
    fun deleteUser(user: UserEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUserRepos(userRepos: List<RepoEntity>)

    @Query("DELETE FROM Repo WHERE ownerUsername=:ownerUsername")
    fun deleteAllReposByOwnerUsername(ownerUsername: String)

    @Transaction
    fun insertUserWithRepos(userWithRepos: UserWithReposEntity) {
        insertUser(userWithRepos.user)
        insertUserRepos(userWithRepos.userRepos)
    }

    @Transaction
    fun deleteUserWithRepos(user: UserEntity) {
        deleteUser(user)
        deleteAllReposByOwnerUsername(user.login)
    }
}