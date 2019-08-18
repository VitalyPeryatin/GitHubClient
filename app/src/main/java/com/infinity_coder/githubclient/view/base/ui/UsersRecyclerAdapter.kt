package com.infinity_coder.githubclient.view.base.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.infinity_coder.githubclient.R
import com.infinity_coder.githubclient.data.base.model.User
import com.infinity_coder.githubclient.presentation.profile.const.OFFLINE_STATE_VALUE
import com.infinity_coder.githubclient.presentation.profile.const.ONLINE_STATE_VALUE
import com.squareup.picasso.Picasso
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_user.view.*
import java.io.File
import java.util.*

class UsersRecyclerAdapter(
    private val networkMode: String,
    var itemClickListener: OnItemClickListener? = null
) : RecyclerView.Adapter<UsersRecyclerAdapter.ViewHolder>() {

    private var users: List<User> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return ViewHolder(
            view,
            networkMode
        )
    }

    override fun getItemCount(): Int =
        users.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(users[position], itemClickListener)

    fun updateUserList(users: List<User>) {
        this.users = users
        notifyDataSetChanged()
    }

    class ViewHolder(
        view: View,
        private val networkMode: String
    ) : RecyclerView.ViewHolder(view), LayoutContainer {
        override val containerView: View? get() = itemView

        fun bind(user: User, itemClickListener: OnItemClickListener?) {
            loadImageInto(user.avatarUrl, itemView.avatarImageView)
            itemView.userLoginTextView.text = user.login
            itemView.userCardView.setOnClickListener { itemClickListener?.onItemClick(user) }
        }

        private fun loadImageInto(url: String?, imageView: ImageView) {
            if (url != null) {
                if (networkMode == ONLINE_STATE_VALUE) {
                    onlineLoadImage(url, imageView)
                } else if (networkMode == OFFLINE_STATE_VALUE) {
                    offlineLoadImage(url, imageView)
                }
            }
        }

        private fun onlineLoadImage(url: String, imageView: ImageView) =
            Picasso.get()
                .load(url)
                .placeholder(R.drawable.ic_avatar_placeholder)
                .into(imageView)

        private fun offlineLoadImage(url: String, imageView: ImageView) =
            Picasso.get()
                .load(File(url))
                .placeholder(R.drawable.ic_avatar_placeholder)
                .into(imageView)
    }

    interface OnItemClickListener {
        fun onItemClick(user: User)
    }
}