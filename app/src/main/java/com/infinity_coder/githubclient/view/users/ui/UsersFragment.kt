package com.infinity_coder.githubclient.view.users.ui

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
import com.infinity_coder.githubclient.presentation.base.MainViewModel
import com.infinity_coder.githubclient.presentation.base.ScreenState
import com.infinity_coder.githubclient.presentation.profile.const.ONLINE_STATE_VALUE
import com.infinity_coder.githubclient.presentation.users.UsersViewModel
import com.infinity_coder.githubclient.presentation.users.UsersViewModelFactory
import com.infinity_coder.githubclient.view.base.MainActivity
import com.infinity_coder.githubclient.view.base.ui.UsersRecyclerAdapter
import com.infinity_coder.githubclient.view.injection.base.Injections
import com.infinity_coder.githubclient.view.profile.const.NETWORK_STATE_KEY
import com.infinity_coder.githubclient.view.profile.const.USER_LOGIN_KEY
import com.infinity_coder.githubclient.view.profile.ui.ProfileFragment
import com.infinity_coder.githubclient.view.saved_users.ui.SavedUsersFragment
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.fragment_users.*
import kotlinx.android.synthetic.main.layout_error.*
import kotlinx.android.synthetic.main.layout_waiting.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class UsersFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: UsersViewModelFactory

    private lateinit var viewModel: UsersViewModel
    private lateinit var sharedViewModel: MainViewModel
    private val menuDisposableBag = CompositeDisposable()
    private var searchMenu: MenuItem? = null
    private var searchView: SearchView? = null
    private val searchUsersRecyclerAdapter = UsersRecyclerAdapter(ONLINE_STATE_VALUE)

    private fun getProfileBundle(user: User): Bundle =
        Bundle().apply {
            putString(USER_LOGIN_KEY, user.login)
            putString(NETWORK_STATE_KEY, ONLINE_STATE_VALUE)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        Injections.inject(this)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(UsersViewModel::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        sharedViewModel = (activity as MainActivity).viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).setSupportActionBar(savedUsersToolbar)

        provideRecyclerItemClickListener()
        usersRecyclerView.layoutManager = LinearLayoutManager(context!!)
        usersRecyclerView.adapter = searchUsersRecyclerAdapter

        observe()

        viewModel.usersLiveData.postValue(emptyList())
        viewModel.screenModeLiveData.postValue(ScreenState.EMPTY)
    }

    private fun provideRecyclerItemClickListener() {
        searchUsersRecyclerAdapter.itemClickListener = object :
            UsersRecyclerAdapter.OnItemClickListener {
            override fun onItemClick(user: User) {
                searchMenu?.collapseActionView()
                showProfileFragment(user)
            }
        }
    }

    private fun showProfileFragment(user: User) {
        (activity as MainActivity).navigate(
            fragment = ProfileFragment().apply {
                arguments = getProfileBundle(user)
            },
            addToBackStack = true
        )
    }

    private fun observe() {
        viewModel.usersLiveData.observe(this, Observer { users ->
            searchUsersRecyclerAdapter.updateUserList(users)
        })
        viewModel.screenModeLiveData.observe(this, Observer { screenState ->
            updateScreen(screenState)
        })
    }

    private fun updateScreen(screenState: ScreenState) {
        when (screenState) {
            ScreenState.RESULT_OK -> {
                usersRecyclerView.visibility = View.VISIBLE
                waitingLayout.visibility = View.GONE
                emptyLayout.visibility = View.GONE
                errorLayout.visibility = View.GONE
            }
            ScreenState.WAITING -> {
                usersRecyclerView.visibility = View.GONE
                waitingLayout.visibility = View.VISIBLE
                emptyLayout.visibility = View.GONE
                errorLayout.visibility = View.GONE
            }
            ScreenState.EMPTY -> {
                usersRecyclerView.visibility = View.GONE
                waitingLayout.visibility = View.GONE
                emptyLayout.visibility = View.VISIBLE
                errorLayout.visibility = View.GONE
            }
            ScreenState.RESULT_ERROR -> {
                usersRecyclerView.visibility = View.GONE
                waitingLayout.visibility = View.GONE
                emptyLayout.visibility = View.GONE
                errorLayout.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_users, container, false)

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.search_users, menu)

        searchMenu = menu?.findItem(R.id.menu_action_search)
        searchView = searchMenu?.actionView as SearchView

        observeSearchViewQueryChange(searchView!!)
        expandSearchViewIfHasNotEmptyQuery(searchMenu!!, searchView!!)
    }

    private fun observeSearchViewQueryChange(searchView: SearchView) {
        val searchViewDisposable = Observable.create<String> { emitter ->
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
        }.debounce(QUERY_DELAY, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .subscribeBy(
                onNext = { usernameBegin ->
                    if (usernameBegin.length >= MIN_USER_QUERY_LENGTH) {
                        sharedViewModel.lastUserListQuery = usernameBegin
                        viewModel.requestUserList(usernameBegin)
                    } else {
                        viewModel.usersLiveData.postValue(emptyList())
                        viewModel.screenModeLiveData.postValue(ScreenState.EMPTY)
                    }
                }
            )
        menuDisposableBag.add(searchViewDisposable)
    }

    private fun expandSearchViewIfHasNotEmptyQuery(menuItem: MenuItem, searchView: SearchView) {
        val lastUserListQuery = sharedViewModel.lastUserListQuery
        if (lastUserListQuery.isNotEmpty()) {
            menuItem.expandActionView()
            searchView.setQuery(sharedViewModel.lastUserListQuery, false)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_favourites -> {
                showSavedUsersFragment()
            }
            else -> {
                return false
            }
        }
        return true
    }

    private fun showSavedUsersFragment() {
        (activity as MainActivity).navigate(
            fragment = SavedUsersFragment(),
            addToBackStack = true
        )
    }

    override fun onDestroyOptionsMenu() {
        menuDisposableBag.clear()
        super.onDestroyOptionsMenu()
    }

    private companion object {
        const val MIN_USER_QUERY_LENGTH = 3
        const val QUERY_DELAY = 500L
    }
}