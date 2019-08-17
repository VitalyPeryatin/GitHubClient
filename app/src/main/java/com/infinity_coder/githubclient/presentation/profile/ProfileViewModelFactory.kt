package com.infinity_coder.githubclient.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.infinity_coder.githubclient.domain.cached_users.UserCachedInteractor
import com.infinity_coder.githubclient.domain.user_repos.UserReposInteractor
import com.infinity_coder.githubclient.domain.remote_users.SearchUserRemoteInteractor
import javax.inject.Inject

class ProfileViewModelFactory @Inject constructor(
    private val remoteInteractor: SearchUserRemoteInteractor,
    private val cachedInteractor: UserCachedInteractor,
    private val userReposInteractor: UserReposInteractor
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(remoteInteractor, cachedInteractor, userReposInteractor) as T
        }
        throw IllegalArgumentException("No such ViewModel: $modelClass")
    }
}