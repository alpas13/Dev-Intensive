package ru.skillbranch.devintensive.ui.adapters

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.skillbranch.devintensive.databinding.ItemChatSingleBinding
import ru.skillbranch.devintensive.models.data.ChatItem

/**
 * Created by Oleksiy Pasmarnov on 23.10.21
 */
class ChatAdapter(private val listener: (ChatItem) -> Unit) : RecyclerView
.Adapter<ChatAdapter.SingleViewHolder>() {
    var items: List<ChatItem> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleViewHolder {
        val convertView = ItemChatSingleBinding.inflate(
            LayoutInflater
                .from(parent.context), parent, false
        )
        Log.d("M_ChatAdapter", "onCreateViewHolder")
        return SingleViewHolder(convertView)
    }

    override fun onBindViewHolder(holder: SingleViewHolder, position: Int) {
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

    inner class SingleViewHolder(private val convertView: ItemChatSingleBinding) : RecyclerView
    .ViewHolder(convertView.root), ItemTouchViewHolder {

        override fun onItemSelected() {
            this.itemView.setBackgroundColor(Color.LTGRAY)
        }

        override fun onItemCleared() {
            this.itemView.setBackgroundColor(Color.WHITE)
        }

        fun bind(item: ChatItem, listener: (ChatItem) -> Unit) {
            if (item.avatar == null) {
                convertView.ivAvatarSingle.setInitials(item.initials)
            } else {
                //TODO("set drawable")
            }

            convertView.svIndicator.visibility = if (item.isOnline) View.VISIBLE else View.GONE
            with(convertView.tvDateSingle) {
                visibility = if (item.lastMessageDate !== null) View.VISIBLE else View.GONE
                text = item.lastMessageDate
            }
            with(convertView.tvCounterSingle) {
                visibility = if (item.messageCount > 0) View.VISIBLE else View.GONE
                text = item.messageCount.toString()
            }
            convertView.tvTitleSingle.text = item.title
            convertView.tvMessageSingle.text = item.shortDescription
            this.itemView.setOnClickListener { listener.invoke(item) }
        }

    }
}