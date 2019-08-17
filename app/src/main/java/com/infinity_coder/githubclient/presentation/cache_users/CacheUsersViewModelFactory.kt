package com.infinity_coder.githubclient.presentation.cache_users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.infinity_coder.githubclient.domain.cached_users.UserCachedInteractor
import java.lang.IllegalArgumentException
import javax.inject.Inject

class CacheUsersViewModelFactory @Inject constructor(
    private val cachedInteractor: UserCachedInteractor
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CacheUsersViewModel::class.java)) {
            return CacheUsersViewModel(cachedInteractor) as T
        }
        throw IllegalArgumentException("No such ViewModel: $modelClass")
    }
}