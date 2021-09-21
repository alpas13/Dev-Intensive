package ru.skillbranch.devintensive.models

import java.util.*

abstract class BaseMessage(
    val id: String,
    val from: User?,
    val chat: Chat,
    val isIncoming: Boolean = false,
    val date: Date = Date()
) {
    abstract fun formatMessage(): String

    companion object AbstractFactory {
        private var lastId = -1

        fun makeMessage(
            from: User?,
            chat: Chat,
            date: Date = Date(),
            isIncoming: Boolean = false,
            type: String = "text",
            payload: Any?,
        ): BaseMessage {
            lastId++

            return when(type) {
                "image" -> ImageMessage("$lastId", from, chat, isIncoming = isIncoming, date = date, image = payload as String)
                    else -> TextMessage("$lastId", from, chat, isIncoming = isIncoming, date = date, text = payload as String)
            }
        }
    }
}