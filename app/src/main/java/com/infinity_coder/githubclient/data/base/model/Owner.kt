package com.infinity_coder.githubclient.data.base.model

import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName
import com.infinity_coder.githubclient.cache.saved_users.structure.OWNER_FIELD_ID
import com.infinity_coder.githubclient.cache.saved_users.structure.OWNER_FIELD_LOGIN
import com.infinity_coder.githubclient.remote.users.structure.OWNER_GSON_ID
import com.infinity_coder.githubclient.remote.users.structure.OWNER_GSON_LOGIN

class Owner {
    @SerializedName(OWNER_GSON_ID)
    @ColumnInfo(name = OWNER_FIELD_ID)
    var id: Int = 0

    @SerializedName(OWNER_GSON_LOGIN)
    @ColumnInfo(name = OWNER_FIELD_LOGIN)
    var username: String = ""
}