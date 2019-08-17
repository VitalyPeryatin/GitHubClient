package com.infinity_coder.githubclient.presentation.profile

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.infinity_coder.githubclient.cache.user.model.RepoEntity
import com.infinity_coder.githubclient.cache.user.model.UserEntity
import com.infinity_coder.githubclient.cache.user.model.UserWithReposEntity
import com.infinity_coder.githubclient.domain.cached_users.UserCachedInteractor
import com.infinity_coder.githubclient.domain.remote_users.SearchUserRemoteInteractor
import com.infinity_coder.githubclient.domain.user_repos.UserReposInteractor
import com.infinity_coder.githubclient.presentation.ScreenMode
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.subscribeBy

class ProfileViewModel(
    private val remoteInteractor: SearchUserRemoteInteractor,
    private val cachedInteractor: UserCachedInteractor,
    private val usersReposInteractor: UserReposInteractor
) : ViewModel() {

    private val disposableBag = CompositeDisposable()
    val userLiveData = MutableLiveData<UserEntity>()
    val userReposLiveData = MutableLiveData<List<RepoEntity>>()
    val isBookmarkUserLiveData = MutableLiveData<Boolean>()
    val screenModeLiveData = MutableLiveData<ScreenMode>()

    var networkMode = ONLINE_MODE

    fun requestUserData(username: String?) {
        if(username != null) {
            screenModeLiveData.postValue(ScreenMode.WAITING)
            val disposable =
                if (networkMode == ONLINE_MODE) {
                    onlineRequestUserData(username)
                } else {
                    offlineRequestUserData(username)
                }
            disposableBag.add(disposable)
        }
    }

    private fun onlineRequestUserData(username: String): Disposable {
        return remoteInteractor.getUser(username)
            .doOnSuccess { userLiveData.postValue(it) }
            .flatMap { cachedInteractor.hasCachedUser(it) }
            .zipWith(
                usersReposInteractor.getUserRepos(username),
                BiFunction<Boolean, List<RepoEntity>, CompositeOfIsUserCachedAndUserRepoList> { isUserCached, userRepos ->
                    CompositeOfIsUserCachedAndUserRepoList(isUserCached, userRepos)
                }).subscribeBy(
                onSuccess = { c ->
                    isBookmarkUserLiveData.postValue(c.isUserCached)
                    userReposLiveData.postValue(c.userRepo)
                    screenModeLiveData.postValue(ScreenMode.RESULT_OK)
                },
                onError = {
                    Log.d("mLog", it.message.toString())
                    screenModeLiveData.postValue(ScreenMode.RESULT_ERROR)
                }
            )
    }

    private fun offlineRequestUserData(username: String): Disposable {
        return cachedInteractor.getUser(username)
            .doOnSuccess {  userWithRepos ->
                userLiveData.postValue(userWithRepos.user)
                userReposLiveData.postValue(userWithRepos.userRepos)
            }.flatMap { userWithRepos -> cachedInteractor.hasCachedUser(userWithRepos.user) }
            .subscribeBy(
                onSuccess = { isUserCached ->
                    isBookmarkUserLiveData.postValue(isUserCached)
                    screenModeLiveData.postValue(ScreenMode.RESULT_OK)
                },
                onError = {
                    Log.d("mLog", it.message.toString())
                    screenModeLiveData.postValue(ScreenMode.RESULT_ERROR)
                }
            )
    }

    fun changeUserCachedState(user: UserEntity) {
        if (isBookmarkUserLiveData.value == true) {
            removeCachedUser(user)
        } else {
            addCachedUser(user)
        }
    }

    private fun removeCachedUser(user: UserEntity) {
        val disposableRemove = cachedInteractor.removeCachedUser(user)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onComplete = {
                    isBookmarkUserLiveData.postValue(false)
                }
            )
        disposableBag.add(disposableRemove)
    }

    private fun addCachedUser(user: UserEntity) {
        val disposableAdd = cachedInteractor.addCachedUser(UserWithReposEntity(user, userReposLiveData.value!!))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onComplete = {
                    isBookmarkUserLiveData.postValue(true)
                }
            )
        disposableBag.add(disposableAdd)
    }

    fun requestBookmarkIconState(user: UserEntity) {
        val bookmarkStateDisposable = cachedInteractor.hasCachedUser(user)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = { isUserCached ->
                    isBookmarkUserLiveData.postValue(isUserCached)
                },
                onError = {
                    Log.d("mLog", it.message.toString())
                }
            )
        disposableBag.add(bookmarkStateDisposable)
    }

    override fun onCleared() {
        cachedInteractor.applyLast().subscribe()
        disposableBag.clear()
        super.onCleared()
    }

    data class CompositeOfIsUserCachedAndUserRepoList(
        var isUserCached: Boolean? = null,
        var userRepo: List<RepoEntity>? = null
    )

    companion object {
        const val ONLINE_MODE = "online"
        const val OFFLINE_MODE = "offline"
    }
}