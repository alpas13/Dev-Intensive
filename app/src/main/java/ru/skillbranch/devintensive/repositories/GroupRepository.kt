package ru.skillbranch.devintensive.repositories

import ru.skillbranch.devintensive.data.manager.CacheManager
import ru.skillbranch.devintensive.models.data.Chat
import ru.skillbranch.devintensive.models.data.User
import ru.skillbranch.devintensive.models.data.UserItem
import ru.skillbranch.devintensive.utils.DataGenerator

/**
 * Created by Oleksiy Pasmarnov on 28.10.21
 */
object GroupRepository {
    fun loadUsers(): List<User> = DataGenerator.stabUsers
    fun createChat(items: List<UserItem>) {
        val ids = items.map { it.id }
        val users = CacheManager.findUsersById(ids)
        val title = users.map { it.firstName }.joinToString(", ")
        val chat = Chat(CacheManager.nextChatId(), title, users)

        CacheManager.insertChat(chat)
    }
}