package com.infinity_coder.githubclient.presentation.remote_users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.infinity_coder.githubclient.domain.remote_users.SearchUserRemoteInteractor
import javax.inject.Inject

class SearchUsersViewModelFactory @Inject constructor(
    private val searchUsersInteractor: SearchUserRemoteInteractor
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchUsersViewModel::class.java)) {
            return SearchUsersViewModel(searchUsersInteractor) as T
        }
        throw IllegalArgumentException("No such ViewModel: $modelClass")
    }
}