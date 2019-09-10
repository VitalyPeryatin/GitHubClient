package com.infinity_coder.githubclient.presentation.users

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.infinity_coder.githubclient.data.base.model.User
import com.infinity_coder.githubclient.domain.users.interactor.UsersInteractor
import com.infinity_coder.githubclient.presentation.base.ScreenState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy

class UsersViewModel(
    private val searchRemoteUsersInteractor: UsersInteractor
) : ViewModel() {

    private val disposableBag = CompositeDisposable()
    val usersLiveData = MutableLiveData<List<User>>()
    val screenModeLiveData = MutableLiveData<ScreenState>()

    fun requestUserList(usernameStart: String) {
        screenModeLiveData.postValue(ScreenState.WAITING)
        disposableBag += searchRemoteUsersInteractor.getUserList(usernameStart)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = { users ->
                    usersLiveData.postValue(users)
                    setScreenState(users)
                },
                onError = {
                    screenModeLiveData.postValue(ScreenState.RESULT_ERROR)
                }
            )
    }

    private fun setScreenState(users: List<User>) {
        val screenState = if (users.isNotEmpty()) {
            ScreenState.RESULT_OK
        } else {
            ScreenState.EMPTY
        }
        screenModeLiveData.postValue(screenState)
    }
}