package com.infinity_coder.githubclient.view.remote_users

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.infinity_coder.githubclient.R
import com.infinity_coder.githubclient.cache.user.model.UserEntity
import com.infinity_coder.githubclient.di.components.DaggerAppComponent
import com.infinity_coder.githubclient.di.components.DaggerSearchUsersComponent
import com.infinity_coder.githubclient.di.modules.AppModule
import com.infinity_coder.githubclient.presentation.App
import com.infinity_coder.githubclient.presentation.ScreenMode
import com.infinity_coder.githubclient.presentation.main_activity.MainViewModel
import com.infinity_coder.githubclient.presentation.profile.ProfileViewModel.Companion.ONLINE_MODE
import com.infinity_coder.githubclient.presentation.remote_users.SearchUsersViewModel
import com.infinity_coder.githubclient.presentation.remote_users.SearchUsersViewModelFactory
import com.infinity_coder.githubclient.view.cache_users.CacheUsersFragment
import com.infinity_coder.githubclient.view.main_activity.MainActivity
import com.infinity_coder.githubclient.view.profile.ProfileFragment
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.fragment_search_users.*
import kotlinx.android.synthetic.main.layout_error.*
import kotlinx.android.synthetic.main.layout_waiting.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class SearchUsersFragment : Fragment() {

    private lateinit var viewModel: SearchUsersViewModel
    private lateinit var sharedViewModel: MainViewModel

    @Inject
    lateinit var viewModelFactory: SearchUsersViewModelFactory


    private val menuDisposableBag = CompositeDisposable()

    private var searchMenu: MenuItem? = null
    private var searchView: SearchView? = null

    private val searchUsersRecyclerAdapter = SearchUsersRecyclerAdapter(ONLINE_MODE)

    private fun getProfileBundle(user: UserEntity): Bundle =
        Bundle().apply {
            putString("userLogin", user.login)
            putString("networkMode", ONLINE_MODE)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        val appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(App.instance))
            .build()
        DaggerSearchUsersComponent.builder()
            .appComponent(appComponent)
            .build()
            .inject(this)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SearchUsersViewModel::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        sharedViewModel = (activity as MainActivity).viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).setSupportActionBar(searchUsersToolbar)

        searchUsersRecyclerAdapter.itemClickListener = object :
            SearchUsersRecyclerAdapter.OnItemClickListener {
            override fun onItemClick(user: UserEntity) {
                searchMenu?.collapseActionView()
                (activity as MainActivity).navigate( // TODO кастование MainActivity
                    fragment = ProfileFragment().apply {
                        arguments = getProfileBundle(user)
                    },
                    addToBackStack = true
                )
            }
        }
        usersRecyclerView.layoutManager = LinearLayoutManager(context!!)
        usersRecyclerView.adapter = searchUsersRecyclerAdapter

        observe()

        viewModel.usersLiveData.postValue(emptyList())
        viewModel.screenModeLiveData.postValue(ScreenMode.EMPTY)
    }

    private fun observe() {
        viewModel.usersLiveData.observe(this, Observer { users ->
            searchUsersRecyclerAdapter.updateUserList(users)
        })
        viewModel.screenModeLiveData.observe(this, Observer { screenMode ->
            when (screenMode) {
                ScreenMode.RESULT_OK -> {
                    usersRecyclerView.visibility = View.VISIBLE
                    waitingLayout.visibility = View.GONE
                    emptyLayout.visibility = View.GONE
                    errorLayout.visibility = View.GONE
                }
                ScreenMode.WAITING -> {
                    usersRecyclerView.visibility = View.GONE
                    waitingLayout.visibility = View.VISIBLE
                    emptyLayout.visibility = View.GONE
                    errorLayout.visibility = View.GONE
                }
                ScreenMode.EMPTY -> {
                    usersRecyclerView.visibility = View.GONE
                    waitingLayout.visibility = View.GONE
                    emptyLayout.visibility = View.VISIBLE
                    errorLayout.visibility = View.GONE
                }
                ScreenMode.RESULT_ERROR -> {
                    usersRecyclerView.visibility = View.GONE
                    waitingLayout.visibility = View.GONE
                    emptyLayout.visibility = View.GONE
                    errorLayout.visibility = View.VISIBLE
                }
                else -> {
                }
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_search_users, container, false)

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.search_remote_users, menu)

        searchMenu = menu?.findItem(R.id.action_search)
        searchView = searchMenu?.actionView as SearchView

        val searchViewDisposable = Observable.create<String> { emitter ->
            searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(s: String): Boolean = true

                override fun onQueryTextChange(usernameBegin: String): Boolean {
                    emitter.onNext(usernameBegin)
                    return true
                }
            })
        }.debounce(200, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .subscribeBy(
                onNext = { usernameBegin ->
                    if (usernameBegin.length >= 3) {
                        sharedViewModel.lastUserListQuery = usernameBegin
                        viewModel.requestUserList(usernameBegin)
                    } else {
                        viewModel.usersLiveData.postValue(emptyList())
                        viewModel.screenModeLiveData.postValue(ScreenMode.EMPTY)
                    }
                }
            )

        val lastUserListQuery = sharedViewModel.lastUserListQuery
        if (lastUserListQuery.isNotEmpty()) {
            searchMenu?.expandActionView()
            searchView?.setQuery(sharedViewModel.lastUserListQuery, false)
        }
        menuDisposableBag.add(searchViewDisposable)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_favourites -> {
                (activity as MainActivity).navigate(
                    fragment = CacheUsersFragment(),
                    addToBackStack = true
                )
            }
        }
        return true
    }

    override fun onDestroyOptionsMenu() {
        menuDisposableBag.clear()
        super.onDestroyOptionsMenu()
    }
}