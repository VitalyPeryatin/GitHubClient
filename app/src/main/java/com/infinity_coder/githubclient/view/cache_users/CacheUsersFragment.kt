package com.infinity_coder.githubclient.view.cache_users

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
import com.infinity_coder.githubclient.di.components.DaggerCachedUsersComponent
import com.infinity_coder.githubclient.di.modules.AppModule
import com.infinity_coder.githubclient.presentation.App
import com.infinity_coder.githubclient.presentation.ScreenMode
import com.infinity_coder.githubclient.presentation.cache_users.CacheUsersViewModel
import com.infinity_coder.githubclient.presentation.cache_users.CacheUsersViewModelFactory
import com.infinity_coder.githubclient.presentation.profile.ProfileViewModel.Companion.OFFLINE_MODE
import com.infinity_coder.githubclient.view.main_activity.MainActivity
import com.infinity_coder.githubclient.view.profile.ProfileFragment
import com.infinity_coder.githubclient.view.remote_users.SearchUsersRecyclerAdapter
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.fragment_cached_users.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CacheUsersFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: CacheUsersViewModelFactory
    private lateinit var viewModel: CacheUsersViewModel

    private val menuDisposableBag = CompositeDisposable()

    private val searchUsersRecyclerAdapter =
        SearchUsersRecyclerAdapter(OFFLINE_MODE, object :
            SearchUsersRecyclerAdapter.OnItemClickListener {
            override fun onItemClick(user: UserEntity) {
                showProfileFragment(user)
            }
        })

    private fun showProfileFragment(user: UserEntity) {
        (activity as MainActivity).navigate( // TODO кастование MainActivity
            fragment = ProfileFragment().apply {
                arguments = getProfileBundle(user)
            },
            addToBackStack = true
        )
    }

    private fun getProfileBundle(user: UserEntity): Bundle =
        Bundle().apply {
            putString("userLogin", user.login)
            putString("networkMode", OFFLINE_MODE)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        val appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(App.instance))
            .build()
        DaggerCachedUsersComponent.builder()
            .appComponent(appComponent)
            .build()
            .inject(this)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CacheUsersViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_cached_users, container, false)

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.search_cached_users, menu)
        val menuItem = menu?.findItem(R.id.action_search)
        val searchView = menuItem?.actionView as SearchView


        val searchViewDisposable = Observable.create<String> { emitter ->
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(s: String): Boolean = true

                override fun onQueryTextChange(usernameBegin: String): Boolean {
                    emitter.onNext(usernameBegin)
                    return true
                }
            })
        }.debounce(50, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .subscribeBy(
                onNext = { usernameBegin ->
                    viewModel.lastUsersQuery = usernameBegin
                    viewModel.requestUserList(usernameBegin)
                }
            )

        val lastUserListQuery = viewModel.lastUsersQuery
        if (lastUserListQuery.isNotEmpty()) {
            menuItem.expandActionView()
            searchView.setQuery(viewModel.lastUsersQuery, false)
        }

        menuDisposableBag.add(searchViewDisposable)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).setSupportActionBar(searchUsersToolbar)

        usersRecyclerView.layoutManager = LinearLayoutManager(context!!)
        usersRecyclerView.adapter = searchUsersRecyclerAdapter

        observe()

        viewModel.requestUserList("")
    }

    private fun observe() {
        viewModel.usersLiveData.observe(this, Observer { users ->
            searchUsersRecyclerAdapter.updateUserList(users)
        })

        viewModel.screenModeLiveData.observe(this, Observer { screenMode ->
            when (screenMode) {
                ScreenMode.RESULT_OK -> {
                    usersRecyclerView.visibility = View.VISIBLE
                    emptyTextView.visibility = View.GONE
                }
                ScreenMode.EMPTY -> {
                    usersRecyclerView.visibility = View.GONE
                    emptyTextView.visibility = View.VISIBLE
                }
                else -> {
                }
            }
        })
    }

    override fun onDestroyOptionsMenu() {
        menuDisposableBag.clear()
        super.onDestroyOptionsMenu()
    }
}