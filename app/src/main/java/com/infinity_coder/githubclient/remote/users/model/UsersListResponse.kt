package com.infinity_coder.githubclient.remote.users.model

import com.google.gson.annotations.SerializedName
import com.infinity_coder.githubclient.data.base.model.User
import com.infinity_coder.githubclient.remote.users.structure.USER_LIST_GSON_ITEMS

data class UsersListResponse(
    @SerializedName(USER_LIST_GSON_ITEMS)
    var items: List<User> = emptyList()
)