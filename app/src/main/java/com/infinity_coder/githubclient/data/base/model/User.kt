package com.infinity_coder.githubclient.data.base.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.infinity_coder.githubclient.cache.saved_users.structure.*
import com.infinity_coder.githubclient.remote.users.structure.*

@Entity(tableName = USER_TABLE_NAME)
data class User(
    @PrimaryKey
    @SerializedName(USER_GSON_ID)
    @ColumnInfo(name = USER_FIELD_ID)
    var id: Int = 0,

    @SerializedName(USER_GSON_LOGIN)
    @ColumnInfo(name = USER_FIELD_LOGIN)
    var login: String = "",

    @SerializedName(USER_GSON_AVATAR_URL)
    @ColumnInfo(name = USER_FIELD_AVATAR_URL)
    var avatarUrl: String? = null,

    @SerializedName(USER_GSON_NAME)
    @ColumnInfo(name = USER_FIELD_NAME)
    var name: String? = null,

    @SerializedName(USER_GSON_COMPANY)
    @ColumnInfo(name = USER_FIELD_COMPANY)
    var company: String? = null,

    @SerializedName(USER_GSON_LOCATION)
    @ColumnInfo(name = USER_FIELD_LOCATION)
    var location: String? = null,

    @SerializedName(USER_GSON_EMAIL)
    @ColumnInfo(name = USER_FIELD_EMAIL)
    var email: String? = null,

    @SerializedName(USER_GSON_BIO)
    @ColumnInfo(name = USER_FIELD_BIO)
    var bio: String? = null,

    @SerializedName(USER_GSON_PUBLIC_REPOS)
    @ColumnInfo(name = USER_FIELD_PUBLIC_REPOS)
    var publicRepos: Int? = 0,

    @SerializedName(USER_GSON_PUBLIC_FOLLOWERS)
    @ColumnInfo(name = USER_FIELD_PUBLIC_FOLLOWERS)
    var followers: Int? = 0,

    @SerializedName(USER_GSON_PUBLIC_FOLLOWING)
    @ColumnInfo(name = USER_FIELD_PUBLIC_FOLLOWING)
    var following: Int? = 0
)