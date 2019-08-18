package com.infinity_coder.githubclient.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.infinity_coder.githubclient.domain.profile.interactor.ProfileInteractor
import javax.inject.Inject

class ProfileViewModelFactory @Inject constructor(
    private val profileInteractor: ProfileInteractor
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(profileInteractor) as T
        }
        throw IllegalArgumentException("No such ViewModel: $modelClass")
    }
}