package com.infinity_coder.githubclient.presentation.cache_users

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.infinity_coder.githubclient.cache.user.model.UserEntity
import com.infinity_coder.githubclient.domain.cached_users.UserCachedInteractor
import com.infinity_coder.githubclient.presentation.ScreenMode
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class CacheUsersViewModel @Inject constructor(
    private val cachedInteractor: UserCachedInteractor
) : ViewModel() {

    private val disposableBag = CompositeDisposable()
    val usersLiveData = MutableLiveData<List<UserEntity>>()
    val screenModeLiveData = MutableLiveData<ScreenMode>()
    var lastUsersQuery: String = ""

    fun requestUserList(usernameBegin: String) {
        val userListDisposable = cachedInteractor.getUserList(usernameBegin)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = { users ->
                    usersLiveData.postValue(users)
                    if (users.isEmpty()) {
                        screenModeLiveData.postValue(ScreenMode.EMPTY)
                    } else {
                        screenModeLiveData.postValue(ScreenMode.RESULT_OK)
                    }
                },
                onError = {
                    Log.d("myLog", it.message.toString())
                }
            )
        disposableBag.add(userListDisposable)
    }
}