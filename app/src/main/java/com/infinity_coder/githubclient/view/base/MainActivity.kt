package com.infinity_coder.githubclient.view.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import com.infinity_coder.githubclient.R
import com.infinity_coder.githubclient.presentation.base.MainViewModel
import com.infinity_coder.githubclient.view.users.ui.UsersFragment

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        if (savedInstanceState == null) {
            navigate(UsersFragment())
        }
    }

    fun navigate(fragment: Fragment, addToBackStack: Boolean = false) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainer, fragment)
            if (addToBackStack) {
                addToBackStack(null)
            }
            commit()
        }
    }
}
