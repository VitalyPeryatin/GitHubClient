package com.infinity_coder.githubclient.cache.user.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.infinity_coder.githubclient.cache.user.structure.*

@Entity(tableName = REPO_TABLE_NAME)
data class RepoEntity(
    @PrimaryKey
    @ColumnInfo(name = REPO_FIELD_ID)
    var id: Int = 0,

    @SerializedName(REPO_FIELD_OWNER)
    @Embedded(prefix = REPO_PREFIX_OWNER)
    var owner: OwnerEntity? = null,

    @ColumnInfo(name = REPO_FIELD_NAME)
    var name: String? = null,

    @ColumnInfo(name = REPO_FIELD_DESCRIPTION)
    var description: String? = null,

    @ColumnInfo(name = REPO_FIELD_PRIVATE)
    var private: Boolean = false,

    @ColumnInfo(name = REPO_FIELD_LANGUAGE)
    var language: String? = null
)