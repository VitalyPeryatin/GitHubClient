package com.infinity_coder.githubclient.view.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.infinity_coder.githubclient.R
import com.infinity_coder.githubclient.cache.user.model.UserEntity
import com.infinity_coder.githubclient.di.components.DaggerAppComponent
import com.infinity_coder.githubclient.di.components.DaggerProfileComponent
import com.infinity_coder.githubclient.di.modules.AppModule
import com.infinity_coder.githubclient.presentation.App
import com.infinity_coder.githubclient.presentation.ScreenMode
import com.infinity_coder.githubclient.presentation.profile.ProfileViewModel
import com.infinity_coder.githubclient.presentation.profile.ProfileViewModel.Companion.ONLINE_MODE
import com.infinity_coder.githubclient.presentation.profile.ProfileViewModelFactory
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.layout_error.*
import kotlinx.android.synthetic.main.layout_waiting.*
import java.io.File
import javax.inject.Inject

class ProfileFragment : Fragment() {

    private lateinit var viewModel: ProfileViewModel
    @Inject
    lateinit var viewModelFactory: ProfileViewModelFactory
    private var user: UserEntity? = null

    private val userReposRecyclerAdapter = UserReposRecyclerAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(App.instance))
            .build()
        DaggerProfileComponent.builder()
            .appComponent(appComponent)
            .build()
            .inject(this)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ProfileViewModel::class.java)
        viewModel.networkMode = arguments?.getString("networkMode", ONLINE_MODE) ?: ONLINE_MODE
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_profile, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userReposRecyclerView.layoutManager = LinearLayoutManager(context)
        userReposRecyclerView.adapter = userReposRecyclerAdapter

        observeViewModel()

        favouriteImageView.setOnClickListener {
            user?.let { user ->
                viewModel.changeUserCachedState(user)
            }
        }

        val username = arguments?.getString("userLogin")
        viewModel.requestUserData(username)
        tryAgainTextView.setOnClickListener {
            viewModel.requestUserData(username)
        }
    }

    private fun observeViewModel() {
        viewModel.userLiveData.observe(this, Observer { user ->
            this.user = user
            bindUser(user)
            requestBookmarkIconState(user)
        })

        viewModel.userReposLiveData.observe(this, Observer { userRepos ->
            userReposRecyclerAdapter.updateUserRepos(userRepos)
        })

        viewModel.isBookmarkUserLiveData.observe(this, Observer {
            if (it == true) {
                favouriteImageView.setImageResource(R.drawable.ic_bookmark)
            } else {
                favouriteImageView.setImageResource(R.drawable.ic_bookmark_border)
            }
        })

        viewModel.screenModeLiveData.observe(this, Observer { screenMode ->
            when (screenMode) {
                ScreenMode.RESULT_OK -> {
                    profileLayout.visibility = View.VISIBLE
                    waitingLayout.visibility = View.GONE
                    errorLayout.visibility = View.GONE
                }
                ScreenMode.WAITING -> {
                    profileLayout.visibility = View.GONE
                    waitingLayout.visibility = View.VISIBLE
                    errorLayout.visibility = View.GONE
                }
                ScreenMode.RESULT_ERROR -> {
                    profileLayout.visibility = View.GONE
                    waitingLayout.visibility = View.GONE
                    errorLayout.visibility = View.VISIBLE
                }
                else -> {
                }
            }
        })
    }

    private fun bindUser(user: UserEntity) {
        loadImageInto(user.avatarUrl, avatarImageView)
        followersValueTextView.text = user.followers.toString()
        followingValueTextView.text = user.following.toString()
        nameTextView.text = user.name
        aboutMeTextView.text = user.bio
    }

    private fun loadImageInto(url: String?, imageView: ImageView) {
        if (url != null) {
            if (viewModel.networkMode == ONLINE_MODE) {
                Picasso.get()
                    .load(url)
                    .placeholder(R.drawable.ic_avatar_placeholder)
                    .into(imageView)
            } else {
                Picasso.get()
                    .load(File(url))
                    .placeholder(R.drawable.ic_avatar_placeholder)
                    .into(imageView)
            }
        }
    }

    private fun requestBookmarkIconState(user: UserEntity) {
        viewModel.requestBookmarkIconState(user)
    }
}