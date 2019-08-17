package com.infinity_coder.githubclient.cache.user.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.infinity_coder.githubclient.cache.user.structure.*

@Entity(tableName = USER_TABLE_NAME)
data class UserEntity (
    @PrimaryKey
    @SerializedName(USER_FIELD_ID)
    @ColumnInfo(name = USER_FIELD_ID)
    var id: Int = 0,

    @SerializedName(USER_FIELD_LOGIN)
    @ColumnInfo(name = USER_FIELD_LOGIN)
    var login: String = "",

    @SerializedName(USER_FIELD_AVATAR_URL)
    @ColumnInfo(name = USER_FIELD_AVATAR_URL)
    var avatarUrl: String? = null,

    @SerializedName(USER_FIELD_NAME)
    @ColumnInfo(name = USER_FIELD_NAME)
    var name: String? = null,

    @SerializedName(USER_FIELD_COMPANY)
    @ColumnInfo(name = USER_FIELD_COMPANY)
    var company: String? = null,

    @SerializedName(USER_FIELD_LOCATION)
    @ColumnInfo(name = USER_FIELD_LOCATION)
    var location: String? = null,

    @SerializedName(USER_FIELD_EMAIL)
    @ColumnInfo(name = USER_FIELD_EMAIL)
    var email: String? = null,

    @SerializedName(USER_FIELD_BIO)
    @ColumnInfo(name = USER_FIELD_BIO)
    var bio: String? = null,

    @SerializedName(USER_FIELD_PUBLIC_REPOS)
    @ColumnInfo(name = USER_FIELD_PUBLIC_REPOS)
    var publicRepos: Int? = 0,

    @SerializedName(USER_FIELD_PUBLIC_FOLLOWERS)
    @ColumnInfo(name = USER_FIELD_PUBLIC_FOLLOWERS)
    var followers: Int? = 0,

    @SerializedName(USER_FIELD_PUBLIC_FOLLOWING)
    @ColumnInfo(name = USER_FIELD_PUBLIC_FOLLOWING)
    var following: Int? = 0
)