package com.infinity_coder.githubclient.cache.saved_users.dao

import androidx.room.*
import com.infinity_coder.githubclient.data.base.model.Repo
import com.infinity_coder.githubclient.data.base.model.User
import com.infinity_coder.githubclient.data.base.model.UserWithRepos

@Dao
interface UserWithReposEntityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUserRepos(userRepos: List<Repo>)

    @Delete
    fun deleteUser(user: User)

    @Query("DELETE FROM Repo WHERE ownerUsername=:ownerUsername")
    fun deleteAllReposByOwnerUsername(ownerUsername: String)

    @Transaction
    fun deleteUserWithRepos(user: User) {
        deleteUser(user)
        deleteAllReposByOwnerUsername(user.login)
    }

    @Transaction
    fun insertUserWithRepos(userWithRepos: UserWithRepos) {
        insertUser(userWithRepos.user)
        insertUserRepos(userWithRepos.userRepos)
    }
}