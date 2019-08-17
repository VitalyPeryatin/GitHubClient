package com.infinity_coder.githubclient.presentation.remote_users

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.infinity_coder.githubclient.cache.user.model.UserEntity
import com.infinity_coder.githubclient.domain.remote_users.SearchUserRemoteInteractor
import com.infinity_coder.githubclient.presentation.ScreenMode
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy

class SearchUsersViewModel(
    private val searchRemoteUsersInteractor: SearchUserRemoteInteractor
) : ViewModel() {

    private val disposableBag = CompositeDisposable()
    val usersLiveData = MutableLiveData<List<UserEntity>>()
    val screenModeLiveData = MutableLiveData<ScreenMode>()

    fun requestUserList(usernameStart: String) {
        screenModeLiveData.postValue(ScreenMode.WAITING)
        val userListDisposable = searchRemoteUsersInteractor.getUserList(usernameStart)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = { users ->
                    usersLiveData.postValue(users)
                    if (users.isNotEmpty()) {
                        screenModeLiveData.postValue(ScreenMode.RESULT_OK)
                    } else {
                        screenModeLiveData.postValue(ScreenMode.EMPTY)
                    }
                },
                onError = {
                    screenModeLiveData.postValue(ScreenMode.RESULT_ERROR)
                }
            )
        disposableBag.add(userListDisposable)
    }
}