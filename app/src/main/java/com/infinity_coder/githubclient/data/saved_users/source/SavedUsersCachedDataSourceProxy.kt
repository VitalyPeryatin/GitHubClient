package com.infinity_coder.githubclient.data.saved_users.source

import io.reactivex.Completable

interface SavedUsersCachedDataSourceProxy :
    SavedUsersCacheDataSource {
    fun applyLast(): Completable
}