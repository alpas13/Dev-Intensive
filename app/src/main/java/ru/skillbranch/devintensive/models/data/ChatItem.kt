package ru.skillbranch.devintensive.models.data

/**
 * Created by Oleksiy Pasmarnov on 23.10.21
 */
data class ChatItem(
    val id: String,
    val avatar: String?,
    val initials: String,
    val title: String,
    val shortDescription: String?,
    val messageCount: Int = 0,
    val lastMessageDate: String?,
    val isOnline: Boolean = false,
    val chatType: ChatType = ChatType.SINGLE,
    val author: String? = null
)