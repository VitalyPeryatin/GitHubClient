package com.infinity_coder.githubclient.view.profile.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.infinity_coder.githubclient.R
import com.infinity_coder.githubclient.data.base.model.Repo
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_repo.view.*
import java.util.ArrayList

class UserReposRecyclerAdapter : RecyclerView.Adapter<UserReposRecyclerAdapter.ViewHolder>() {

    private var userRepos: List<Repo> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_repo, parent, false)
        return ViewHolder(
            itemView
        )
    }

    override fun getItemCount(): Int =
        userRepos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(userRepos[position])

    fun updateUserRepos(userRepos: List<Repo>) {
        this.userRepos = userRepos
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view), LayoutContainer {
        override val containerView: View?
            get() = itemView

        fun bind(userRepo: Repo) {
            itemView.titleTextView.setNotNullTextOrHide(userRepo.name)
            itemView.descriptionTextView.setNotNullTextOrHide(userRepo.description)
            itemView.languageTextView.setNotNullTextOrHide(userRepo.language)
        }

        private fun TextView.setNotNullTextOrHide(text: String?) {
            if (text == null) {
                visibility = View.GONE
            } else {
                visibility = View.VISIBLE
                setText(text)
            }
        }
    }
}