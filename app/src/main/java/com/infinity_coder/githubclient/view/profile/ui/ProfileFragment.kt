package com.infinity_coder.githubclient.view.profile.ui

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.infinity_coder.githubclient.R
import com.infinity_coder.githubclient.data.base.model.User
import com.infinity_coder.githubclient.presentation.base.ScreenState
import com.infinity_coder.githubclient.presentation.profile.ProfileViewModel
import com.infinity_coder.githubclient.presentation.profile.ProfileViewModelFactory
import com.infinity_coder.githubclient.presentation.profile.const.ONLINE_STATE_VALUE
import com.infinity_coder.githubclient.view.injection.base.Injections
import com.infinity_coder.githubclient.view.profile.const.NETWORK_STATE_KEY
import com.infinity_coder.githubclient.view.profile.const.USER_LOGIN_KEY
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.layout_app_bar_profile.*
import kotlinx.android.synthetic.main.layout_error.*
import kotlinx.android.synthetic.main.layout_waiting.*
import java.io.File
import javax.inject.Inject

class ProfileFragment : Fragment() {

    private lateinit var viewModel: ProfileViewModel
    @Inject
    lateinit var viewModelFactory: ProfileViewModelFactory
    private var user: User? = null
    private var bookmarkMenuItem: MenuItem? = null

    private val userReposRecyclerAdapter =
        UserReposRecyclerAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        Injections.inject(this)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ProfileViewModel::class.java)
        viewModel.networkMode = arguments?.getString(
            NETWORK_STATE_KEY,
            ONLINE_STATE_VALUE
        ) ?: ONLINE_STATE_VALUE
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_profile, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userReposRecyclerView.layoutManager = LinearLayoutManager(context)
        userReposRecyclerView.adapter = userReposRecyclerAdapter

        observeViewModel()

        val username = arguments?.getString(USER_LOGIN_KEY)
        viewModel.requestUserDataIfUsernameNotNull(username)
        tryAgainTextView.setOnClickListener {
            viewModel.requestUserDataIfUsernameNotNull(username)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val parentActivity = activity as AppCompatActivity
        initActionBar(parentActivity)
    }

    private fun initActionBar(activity: AppCompatActivity) {
        activity.setSupportActionBar(profileToolbar)
        val actionBar = activity.supportActionBar
        actionBar?.run {
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
        profileToolbar.run {
            setNavigationIcon(R.drawable.ic_arrow_back)
            setNavigationOnClickListener {
                activity.onBackPressed()
            }
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
                checkBookmarkMenuItem()
            } else {
                uncheckBookmarkMenuItem()
            }
        })

        viewModel.screenModeLiveData.observe(this, Observer { screenMode ->
            when (screenMode) {
                ScreenState.RESULT_OK -> {
                    profileLayout.visibility = View.VISIBLE
                    waitingLayout.visibility = View.GONE
                    errorLayout.visibility = View.GONE
                }
                ScreenState.WAITING -> {
                    profileLayout.visibility = View.GONE
                    waitingLayout.visibility = View.VISIBLE
                    errorLayout.visibility = View.GONE
                }
                ScreenState.RESULT_ERROR -> {
                    profileLayout.visibility = View.GONE
                    waitingLayout.visibility = View.GONE
                    errorLayout.visibility = View.VISIBLE
                }
                else -> {
                }
            }
        })
    }

    private fun bindUser(user: User) {
        loadImage(user.avatarUrl, avatarImageView)
        followersValueTextView.text = user.followers.toString()
        followingValueTextView.text = user.following.toString()
        nameTextView.text = user.name
        aboutMeTextView.text = user.bio
    }

    private fun loadImage(url: String?, imageView: ImageView) {
        if (url != null) {
            if (viewModel.networkMode == ONLINE_STATE_VALUE) {
                onlineLoadImage(url, imageView)
            } else {
                offlineLoadImage(url, imageView)
            }
        }
    }

    private fun onlineLoadImage(url: String, imageView: ImageView) {
        Picasso.get()
            .load(url)
            .placeholder(R.drawable.ic_avatar_placeholder)
            .into(imageView)
    }

    private fun offlineLoadImage(url: String, imageView: ImageView) {
        Picasso.get()
            .load(File(url))
            .placeholder(R.drawable.ic_avatar_placeholder)
            .into(imageView)
    }

    private fun requestBookmarkIconState(user: User) =
        viewModel.requestBookmarkIconState(user)

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.profile_bookmark, menu)
        bookmarkMenuItem = menu?.findItem(R.id.menu_bookmark)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_bookmark -> {
                user?.let { user ->
                    viewModel.changeUserSavedState(user)
                }
            }
            else -> {
                return false
            }
        }
        return true
    }

    private fun checkBookmarkMenuItem() =
        bookmarkMenuItem?.setIcon(R.drawable.ic_bookmark)

    private fun uncheckBookmarkMenuItem() =
        bookmarkMenuItem?.setIcon(R.drawable.ic_bookmark_border)
}