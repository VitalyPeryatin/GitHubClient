package com.infinity_coder.githubclient.data.base.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.infinity_coder.githubclient.cache.saved_users.structure.*
import com.infinity_coder.githubclient.remote.users.structure.*

@Entity(tableName = REPO_TABLE_NAME)
data class Repo(
    @PrimaryKey
    @SerializedName(REPO_GSON_ID)
    @ColumnInfo(name = REPO_FIELD_ID)
    var id: Int = 0,

    @SerializedName(REPO_GSON_OWNER)
    @Embedded(prefix = REPO_PREFIX_OWNER)
    var owner: Owner? = null,

    @SerializedName(REPO_GSON_NAME)
    @ColumnInfo(name = REPO_FIELD_NAME)
    var name: String? = null,

    @SerializedName(REPO_GSON_DESCRIPTION)
    @ColumnInfo(name = REPO_FIELD_DESCRIPTION)
    var description: String? = null,

    @SerializedName(REPO_GSON_PRIVATE)
    @ColumnInfo(name = REPO_FIELD_PRIVATE)
    var private: Boolean = false,

    @SerializedName(REPO_GSON_LANGUAGE)
    @ColumnInfo(name = REPO_FIELD_LANGUAGE)
    var language: String? = null
)