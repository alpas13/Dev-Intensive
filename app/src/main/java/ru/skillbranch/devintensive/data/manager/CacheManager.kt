package ru.skillbranch.devintensive.data.manager

import androidx.lifecycle.MutableLiveData
import ru.skillbranch.devintensive.extensions.mutableLiveData
import ru.skillbranch.devintensive.models.data.Chat
import ru.skillbranch.devintensive.utils.DataGenerator

/**
 * Created by Oleksiy Pasmarnov on 27.10.21
 */
object CacheManager {
    private val chats = mutableLiveData(DataGenerator.stabChats)

    fun loadChats(): MutableLiveData<List<Chat>> {
        return chats
    }
}