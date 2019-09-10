package com.infinity_coder.githubclient.view.saved_users.ui

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.infinity_coder.githubclient.R
import com.infinity_coder.githubclient.data.base.model.User
import com.infinity_coder.githubclient.presentation.base.ScreenState
import com.infinity_coder.githubclient.presentation.profile.const.OFFLINE_STATE_VALUE
import com.infinity_coder.githubclient.presentation.saved_users.SavedUsersViewModel
import com.infinity_coder.githubclient.presentation.saved_users.SavedUsersViewModelFactory
import com.infinity_coder.githubclient.view.base.MainActivity
import com.infinity_coder.githubclient.view.base.ui.UsersRecyclerAdapter
import com.infinity_coder.githubclient.view.injection.base.Injections
import com.infinity_coder.githubclient.view.profile.const.NETWORK_STATE_KEY
import com.infinity_coder.githubclient.view.profile.const.USER_LOGIN_KEY
import com.infinity_coder.githubclient.view.profile.ui.ProfileFragment
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.fragment_saved_users.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SavedUsersFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: SavedUsersViewModelFactory
    private lateinit var viewModel: SavedUsersViewModel

    private val menuDisposableBag = CompositeDisposable()

    private val searchUsersRecyclerAdapter =
        UsersRecyclerAdapter(
            OFFLINE_STATE_VALUE,
            object :
                UsersRecyclerAdapter.OnItemClickListener {
                override fun onItemClick(user: User) {
                    showProfileFragment(user)
                }
            })

    private fun showProfileFragment(user: User) {
        (activity as MainActivity).navigate(
            fragment = ProfileFragment().apply {
                arguments = getProfileBundle(user)
            },
            addToBackStack = true
        )
    }

    private fun getProfileBundle(user: User): Bundle =
        Bundle().apply {
            putString(USER_LOGIN_KEY, user.login)
            putString(NETWORK_STATE_KEY, OFFLINE_STATE_VALUE)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        Injections.inject(this)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(SavedUsersViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_saved_users, container, false)

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.search_saved_users, menu)

        val menuItem = menu?.findItem(R.id.menu_action_search)
        val searchView = menuItem?.actionView as SearchView

        observeSearchViewQueryChange(searchView)
        expandSearchViewIfHasNotEmptyQuery(menuItem, searchView)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val parentActivity = activity as AppCompatActivity
        initActionBar(parentActivity)
    }

    private fun initActionBar(activity: AppCompatActivity) {
        activity.setSupportActionBar(savedUsersToolbar)
        val actionBar = activity.supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)

        savedUsersToolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        savedUsersToolbar.setNavigationOnClickListener {
            activity.onBackPressed()
        }
    }

    private fun observeSearchViewQueryChange(searchView: SearchView) {
        menuDisposableBag += Observable.create<String> { emitter ->
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(usernameBegin: String): Boolean {
                    searchView.clearFocus()
                    emitter.onNext(usernameBegin)
                    return true
                }

                override fun onQueryTextChange(usernameBegin: String): Boolean {
                    emitter.onNext(usernameBegin)
                    return true
                }
            })
        }.debounce(100, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .subscribeBy(
                onNext = { usernameBegin ->
                    viewModel.lastUsersQuery = usernameBegin
                    viewModel.requestUserList(usernameBegin)
                }
            )
    }

    private fun expandSearchViewIfHasNotEmptyQuery(menuItem: MenuItem, searchView: SearchView) {
        val lastUserListQuery = viewModel.lastUsersQuery
        if (lastUserListQuery.isNotEmpty()) {
            menuItem.expandActionView()
            searchView.setQuery(viewModel.lastUsersQuery, false)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).setSupportActionBar(savedUsersToolbar)

        usersRecyclerView.layoutManager = LinearLayoutManager(context!!)
        usersRecyclerView.adapter = searchUsersRecyclerAdapter

        observe()

        viewModel.requestUserList("")
    }

    private fun observe() {
        viewModel.usersLiveData.observe(this, Observer { users ->
            searchUsersRecyclerAdapter.updateUserList(users)
        })

        viewModel.screenModeLiveData.observe(this, Observer { screenState ->
            updateScreen(screenState)
        })
    }

    private fun updateScreen(screenState: ScreenState) =
        when (screenState) {
            ScreenState.RESULT_OK -> {
                usersRecyclerView.visibility = View.VISIBLE
                emptyTextView.visibility = View.GONE
            }
            ScreenState.EMPTY -> {
                usersRecyclerView.visibility = View.GONE
                emptyTextView.visibility = View.VISIBLE
            }
            else -> {
            }
        }

    override fun onDestroyOptionsMenu() {
        menuDisposableBag.clear()
        super.onDestroyOptionsMenu()
    }
}