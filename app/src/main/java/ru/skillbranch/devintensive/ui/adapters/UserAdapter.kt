package ru.skillbranch.devintensive.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.skillbranch.devintensive.models.data.UserItem
import ru.skillbranch.devintensive.databinding.ItemUserListBinding

/**
 * Created by Oleksiy Pasmarnov on 28.10.21
 */
class UserAdapter(val listener: (UserItem) -> Unit) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    var items: List<UserItem> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder =
        UserViewHolder(
            ItemUserListBinding.inflate(
                LayoutInflater
                    .from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) =
        holder.bind(items[position], listener)

    override fun getItemCount(): Int = items.size

    fun updateData(data: List<UserItem>) {
        val diffCallback = object : DiffUtil.Callback() {
            override fun getOldListSize(): Int = items.size

            override fun getNewListSize(): Int = data.size

            override fun areItemsTheSame(oldPos: Int, newPos: Int): Boolean =
                items[oldPos].id == data[newPos].id

            override fun areContentsTheSame(oldPos: Int, newPos: Int): Boolean =
                items[oldPos].hashCode() == data[newPos].hashCode()
        }

        val diffResult = DiffUtil.calculateDiff(diffCallback)

        items = data
        diffResult.dispatchUpdatesTo(this)
    }

    inner class UserViewHolder(private val convertView: ItemUserListBinding) :
        RecyclerView.ViewHolder(convertView.root) {

        fun bind(user: UserItem, listener: (UserItem) -> Unit) {
            convertView.apply {
                if (user.avatar != null) {
                    Glide.with(itemView)
                        .load(user.avatar)
                        .into(ivAvatarUser)
                } else {
                    Glide.with(itemView)
                        .clear(ivAvatarUser)
                    ivAvatarUser.setInitials(user.initials ?: "??")
                }

                svIndicator.visibility = if (user.isOnline) View.VISIBLE else View.GONE
                tvUserName.text = user.fullName
                tvLastActivity.text = user.lastActivity
                ivSelected.visibility = if (user.isSelected) View.VISIBLE else View.GONE
            }
            itemView.setOnClickListener { listener.invoke(user) }
        }
    }
}
