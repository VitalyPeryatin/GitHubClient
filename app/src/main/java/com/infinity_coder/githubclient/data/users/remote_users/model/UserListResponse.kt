package com.infinity_coder.githubclient.data.users.remote_users.model

import com.google.gson.annotations.SerializedName
import com.infinity_coder.githubclient.cache.user.model.UserEntity

class UserListResponse {
    @SerializedName("items")
    var items: List<UserEntity> = emptyList()
}