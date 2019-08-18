package com.infinity_coder.githubclient.cache.saved_users.dao

import androidx.room.Dao
import androidx.room.Query
import com.infinity_coder.githubclient.data.base.model.Repo
import io.reactivex.Single

@Dao
interface RepoEntityDao {
    @Query("SELECT * FROM Repo WHERE ownerUsername=:ownerUsername")
    fun getAllReposByOwnerUsername(ownerUsername: String): Single<List<Repo>>
}