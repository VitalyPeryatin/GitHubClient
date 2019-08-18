package com.infinity_coder.githubclient.data.base.model

import androidx.room.Embedded
import androidx.room.Relation
import com.infinity_coder.githubclient.cache.saved_users.structure.OWNER_FIELD_ID
import com.infinity_coder.githubclient.cache.saved_users.structure.REPO_PREFIX_OWNER
import com.infinity_coder.githubclient.cache.saved_users.structure.USER_FIELD_ID

data class UserWithRepos(
    @Embedded
    var user: User,

    @Relation(parentColumn = USER_FIELD_ID, entityColumn = REPO_PREFIX_OWNER + "_" + OWNER_FIELD_ID)
    var userRepos: List<Repo>
)