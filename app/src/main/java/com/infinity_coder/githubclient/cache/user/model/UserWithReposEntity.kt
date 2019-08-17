package com.infinity_coder.githubclient.cache.user.model

import androidx.room.Embedded
import androidx.room.Relation
import com.infinity_coder.githubclient.cache.user.structure.REPO_FIELD_ID
import com.infinity_coder.githubclient.cache.user.structure.REPO_PREFIX_OWNER
import com.infinity_coder.githubclient.cache.user.structure.USER_FIELD_ID

data class UserWithReposEntity(
    @Embedded
    var user: UserEntity,

    @Relation(parentColumn = USER_FIELD_ID, entityColumn = "owner_id")
    var userRepos: List<RepoEntity>
)