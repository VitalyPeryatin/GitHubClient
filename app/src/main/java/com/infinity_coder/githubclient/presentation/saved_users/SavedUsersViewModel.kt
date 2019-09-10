package com.infinity_coder.githubclient.presentation.saved_users

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.infinity_coder.githubclient.data.base.model.User
import com.infinity_coder.githubclient.domain.saved_users.interactor.SavedUsersInteractor
import com.infinity_coder.githubclient.presentation.base.ScreenState
import com.infinity_coder.githubclient.view.base.const.LOG_ERROR
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class SavedUsersViewModel @Inject constructor(
    private val cachedInteractor: SavedUsersInteractor
) : ViewModel() {

    private val disposableBag = CompositeDisposable()
    val usersLiveData = MutableLiveData<List<User>>()
    val screenModeLiveData = MutableLiveData<ScreenState>()
    var lastUsersQuery: String = ""

    fun requestUserList(usernameBegin: String) {
        disposableBag += cachedInteractor.getCachedUserList(usernameBegin)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = { users ->
                    usersLiveData.postValue(users)
                    setScreenState(users)
                },
                onError = {
                    Log.e(LOG_ERROR, it.message.toString())
                }
            )
    }

    private fun setScreenState(users: List<User>) {
        val screenState = if (users.isEmpty()) {
            ScreenState.EMPTY
        } else {
            ScreenState.RESULT_OK
        }
        screenModeLiveData.postValue(screenState)
    }
}