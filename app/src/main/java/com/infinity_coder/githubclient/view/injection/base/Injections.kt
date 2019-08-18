package com.infinity_coder.githubclient.view.injection.base

import android.content.Context
import androidx.fragment.app.Fragment
import com.infinity_coder.githubclient.view.injection.app.DaggerAppComponent
import com.infinity_coder.githubclient.view.injection.profile.DaggerProfileComponent
import com.infinity_coder.githubclient.view.injection.saved_users.DaggerSavedUsersComponent
import com.infinity_coder.githubclient.view.injection.users.DaggerUsersComponent
import com.infinity_coder.githubclient.view.profile.ui.ProfileFragment
import com.infinity_coder.githubclient.view.saved_users.ui.SavedUsersFragment
import com.infinity_coder.githubclient.view.users.ui.UsersFragment

object Injections {
    fun inject(profileFragment: ProfileFragment) {
        val appComponent = DaggerAppComponent.builder()
            .appContext(getAppContext(profileFragment))
            .build()
        DaggerProfileComponent.builder()
            .appComponent(appComponent)
            .build()
            .inject(profileFragment)
    }

    fun inject(savedUsersFragment: SavedUsersFragment) {
        val appComponent = DaggerAppComponent.builder()
            .appContext(getAppContext(savedUsersFragment))
            .build()
        DaggerSavedUsersComponent.builder()
            .appComponent(appComponent)
            .build()
            .inject(savedUsersFragment)
    }

    fun inject(usersFragment: UsersFragment) {
        val appComponent = DaggerAppComponent.builder()
            .appContext(getAppContext(usersFragment))
            .build()
        DaggerUsersComponent.builder()
            .appComponent(appComponent)
            .build()
            .inject(usersFragment)
    }

    private fun getAppContext(fragment: Fragment): Context =
        fragment.activity!!.applicationContext
}