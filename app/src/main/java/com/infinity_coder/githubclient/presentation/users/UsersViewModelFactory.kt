package com.infinity_coder.githubclient.presentation.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.infinity_coder.githubclient.domain.users.interactor.UsersInteractor
import javax.inject.Inject

class UsersViewModelFactory @Inject constructor(
    private val searchUsersInteractor: UsersInteractor
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UsersViewModel::class.java)) {
            return UsersViewModel(searchUsersInteractor) as T
        }
        throw IllegalArgumentException("No such ViewModel: $modelClass")
    }
}