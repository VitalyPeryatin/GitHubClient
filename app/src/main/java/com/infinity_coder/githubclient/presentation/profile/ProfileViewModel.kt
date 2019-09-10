package com.infinity_coder.githubclient.presentation.profile

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.infinity_coder.githubclient.data.base.model.Repo
import com.infinity_coder.githubclient.data.base.model.User
import com.infinity_coder.githubclient.data.base.model.UserWithRepos
import com.infinity_coder.githubclient.domain.profile.interactor.ProfileInteractor
import com.infinity_coder.githubclient.presentation.base.ScreenState
import com.infinity_coder.githubclient.presentation.profile.const.ONLINE_STATE_VALUE
import com.infinity_coder.githubclient.view.base.const.LOG_ERROR
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy

class ProfileViewModel(
    private val profileInteractor: ProfileInteractor
) : ViewModel() {

    private val disposableBag = CompositeDisposable()

    val userLiveData = MutableLiveData<User>()
    val userReposLiveData = MutableLiveData<List<Repo>>()
    val isBookmarkUserLiveData = MutableLiveData<Boolean>()
    val screenModeLiveData = MutableLiveData<ScreenState>()
    var networkMode = ONLINE_STATE_VALUE

    fun requestUserDataIfUsernameNotNull(username: String?) {
        if (username != null) {
            screenModeLiveData.postValue(ScreenState.WAITING)
            requestUserData(username)
        }
    }

    private fun requestUserData(username: String) {
        disposableBag +=
            if (networkMode == ONLINE_STATE_VALUE) {
                onlineRequestUserData(username)
            } else {
                offlineRequestUserData(username)
            }
    }

    private fun onlineRequestUserData(username: String): Disposable =
        profileInteractor.getUser(username)
            .doOnSuccess { userLiveData.postValue(it) }
            .flatMap { profileInteractor.isUserSaved(it) }
            .zipWith(
                profileInteractor.getUserRepos(username),
                BiFunction<Boolean, List<Repo>, CompositeOfIsUserCachedAndUserRepoList> { isUserCached, userRepos ->
                    CompositeOfIsUserCachedAndUserRepoList(isUserCached, userRepos)
                }).subscribeBy(
                onSuccess = { compositeUserList ->
                    isBookmarkUserLiveData.postValue(compositeUserList.isUserCached)
                    userReposLiveData.postValue(compositeUserList.userRepo)
                    screenModeLiveData.postValue(ScreenState.RESULT_OK)
                },
                onError = {
                    Log.e(LOG_ERROR, it.message.toString())
                    screenModeLiveData.postValue(ScreenState.RESULT_ERROR)
                }
            )

    private fun offlineRequestUserData(username: String): Disposable =
        profileInteractor.getSavedUserWithRepos(username)
            .doOnSuccess { userWithRepos ->
                userLiveData.postValue(userWithRepos.user)
                userReposLiveData.postValue(userWithRepos.userRepos)
            }.flatMap { userWithRepos -> profileInteractor.isUserSaved(userWithRepos.user) }
            .subscribeBy(
                onSuccess = { isUserCached ->
                    isBookmarkUserLiveData.postValue(isUserCached)
                    screenModeLiveData.postValue(ScreenState.RESULT_OK)
                },
                onError = {
                    Log.e(LOG_ERROR, it.message.toString())
                    screenModeLiveData.postValue(ScreenState.RESULT_ERROR)
                }
            )

    fun changeUserSavedState(user: User) =
        if (isBookmarkUserLiveData.value == true) {
            removeCachedUser(user)
        } else {
            addCachedUser(user)
        }

    private fun removeCachedUser(user: User) {
        disposableBag += profileInteractor.removeSavedUser(user)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onComplete = {
                    isBookmarkUserLiveData.postValue(false)
                }
            )
    }

    private fun addCachedUser(user: User) {
        disposableBag += profileInteractor.addSavedUser(
            UserWithRepos(
                user,
                userReposLiveData.value!!
            )
        ).observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onComplete = {
                    isBookmarkUserLiveData.postValue(true)
                }
            )
    }

    fun requestBookmarkIconState(user: User) {
        disposableBag += profileInteractor.isUserSaved(user)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = { isUserCached ->
                    isBookmarkUserLiveData.postValue(isUserCached)
                },
                onError = {
                    Log.e(LOG_ERROR, it.message.toString())
                }
            )
    }

    override fun onCleared() {
        profileInteractor.applyLast().subscribe()
        disposableBag.clear()
        super.onCleared()
    }

    data class CompositeOfIsUserCachedAndUserRepoList(
        var isUserCached: Boolean = false,
        var userRepo: List<Repo> = emptyList()
    )
}