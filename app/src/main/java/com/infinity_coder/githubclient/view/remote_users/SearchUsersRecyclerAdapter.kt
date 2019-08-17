package com.infinity_coder.githubclient.view.remote_users

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.infinity_coder.githubclient.R
import com.infinity_coder.githubclient.cache.user.model.UserEntity
import com.infinity_coder.githubclient.presentation.profile.ProfileViewModel.Companion.ONLINE_MODE
import com.squareup.picasso.Picasso
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_user.view.*
import java.io.File
import java.util.*

class SearchUsersRecyclerAdapter(
    private val networkMode: String,
    var itemClickListener: OnItemClickListener? = null
) : RecyclerView.Adapter<SearchUsersRecyclerAdapter.ViewHolder>() {

    private var users: List<UserEntity> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return ViewHolder(view, networkMode)
    }

    override fun getItemCount(): Int =
        users.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(users[position], itemClickListener)

    fun updateUserList(users: List<UserEntity>) {
        this.users = users
        notifyDataSetChanged()
    }

    class ViewHolder(
        view: View,
        private val networkMode: String
    ) : RecyclerView.ViewHolder(view), LayoutContainer {
        override val containerView: View? get() = itemView

        fun bind(user: UserEntity, itemClickListener: OnItemClickListener?) {
            loadImageInto(user.avatarUrl, itemView.avatarImageView)
            itemView.userLoginTextView.text = user.login
            itemView.setOnClickListener { itemClickListener?.onItemClick(user) }
        }

        private fun loadImageInto(url: String?, imageView: ImageView) {
            if (url != null) {
                if (networkMode == ONLINE_MODE) {
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
    }

    interface OnItemClickListener {
        fun onItemClick(user: UserEntity)
    }
}