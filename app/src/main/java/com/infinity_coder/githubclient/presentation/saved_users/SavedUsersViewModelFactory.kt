package com.infinity_coder.githubclient.presentation.saved_users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.infinity_coder.githubclient.domain.saved_users.interactor.SavedUsersInteractor
import java.lang.IllegalArgumentException
import javax.inject.Inject

class SavedUsersViewModelFactory @Inject constructor(
    private val cachedInteractor: SavedUsersInteractor
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SavedUsersViewModel::class.java)) {
            return SavedUsersViewModel(cachedInteractor) as T
        }
        throw IllegalArgumentException("No such ViewModel: $modelClass")
    }
}