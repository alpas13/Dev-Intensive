package ru.skillbranch.devintensive.ui.adapters

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import ru.skillbranch.devintensive.databinding.ItemChatSingleBinding
import ru.skillbranch.devintensive.databinding.ItemChatGroupBinding
import ru.skillbranch.devintensive.models.data.ChatItem
import ru.skillbranch.devintensive.models.data.ChatType

/**
 * Created by Oleksiy Pasmarnov on 23.10.21
 */
class ChatAdapter(private val listener: (ChatItem) -> Unit) : RecyclerView
.Adapter<ChatAdapter.ChatItemViewHolder>() {
    var items: List<ChatItem> = listOf()

    companion object {
        private const val ARCHIVE_TYPE = 0
        private const val SINGLE_TYPE = 1
        private const val GROUP_TYPE = 2
    }

    override fun getItemViewType(position: Int): Int = when (items[position].chatType) {
        ChatType.ARCHIVE -> ARCHIVE_TYPE
        ChatType.SINGLE -> SINGLE_TYPE
        ChatType.GROUP -> GROUP_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatItemViewHolder {
        return when (viewType) {
            SINGLE_TYPE -> SingleViewHolder(ItemChatSingleBinding.inflate(
                LayoutInflater
                    .from(parent.context), parent, false
            ))
            GROUP_TYPE -> GroupViewHolder(ItemChatGroupBinding.inflate(
                LayoutInflater
                    .from(parent.context), parent, false
            ))
            else -> SingleViewHolder(ItemChatSingleBinding.inflate(
                LayoutInflater
                    .from(parent.context), parent, false
            ))
        }
    }

    override fun onBindViewHolder(holder: ChatItemViewHolder, position: Int) {
        Log.d("M_ChatAdapter", "onBindViewHolder $position")
        val item = items[position]
        holder.bind(item, listener)
    }

    fun updateData(data: List<ChatItem>) {
        Log.d(
            "M_ChatAdapter", "update data adapter - new data ${data.size} hash :" +
                    "${data.hashCode()} old data ${items.size} hash: ${items.hashCode()}"
        )

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

    override fun getItemCount(): Int {
        Log.d("M_ChatAdapter", "${items.size}")
        return items.size
    }

    abstract class ChatItemViewHolder(convertView: ViewBinding) :
        RecyclerView.ViewHolder(convertView.root) {
        abstract val convertView: ViewBinding
        abstract fun bind(item: ChatItem, listener: (ChatItem) -> Unit)
    }

    inner class GroupViewHolder(
        override val convertView: ItemChatGroupBinding
        ) : ChatItemViewHolder(convertView) {

        override fun bind(item: ChatItem, listener: (ChatItem) -> Unit) {
            convertView.apply {

                ivAvatarGroup.setInitials(item.title[0].toString())

                with(tvDateGroup) {
                    visibility = if (item.lastMessageDate !== null) View.VISIBLE else View.GONE
                    text = item.lastMessageDate
                }

                with(tvCounterGroup) {
                    visibility = if (item.messageCount > 0) View.VISIBLE else View.GONE
                    text = item.messageCount.toString()
                }

                tvTitleGroup.text = item.title
                tvMessageGroup.text = item.shortDescription

                with(tvMessageAuthor) {
                    visibility = if (item.messageCount > 0) View.VISIBLE else View.GONE
                    text = item.author
                }
            }

            itemView.setOnClickListener { listener.invoke(item) }
        }
    }

    inner class SingleViewHolder(
        override val convertView: ItemChatSingleBinding
    ) : ChatItemViewHolder(convertView), ItemTouchViewHolder {

        override fun onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY)
        }

        override fun onItemCleared() {
            itemView.setBackgroundColor(Color.WHITE)
        }

        override fun bind(item: ChatItem, listener: (ChatItem) -> Unit) {

            convertView.apply {
                if (item.avatar == null) {
                    Glide.with(itemView).clear(ivAvatarSingle)
                    ivAvatarSingle.setInitials(item.initials)
                } else {
                    Glide.with(itemView)
                        .load(item.avatar)
                        .into(ivAvatarSingle)
                }

                svIndicator.visibility = if (item.isOnline) View.VISIBLE else View.GONE

                with(tvDateSingle) {
                    visibility = if (item.lastMessageDate !== null) View.VISIBLE else View.GONE
                    text = item.lastMessageDate
                }

                with(tvCounterSingle) {
                    visibility = if (item.messageCount > 0) View.VISIBLE else View.GONE
                    text = item.messageCount.toString()
                }

                tvTitleSingle.text = item.title
                tvMessageSingle.text = item.shortDescription
            }

            itemView.setOnClickListener { listener.invoke(item) }

        }

    }
}