package ru.skillbranch.devintensive.models

import ru.skillbranch.devintensive.models.data.Chat
import ru.skillbranch.devintensive.models.data.User
import java.util.*

class ImageMessage(
    id: String,
    from: User?,
    chat: Chat,
    isIncoming: Boolean = false,
    date: Date = Date(),
    isRead: Boolean = false,
    var image: String
) : BaseMessage(id, from, chat, isIncoming, isRead, date) {
    override fun formatMessage(): String =
        "${from?.firstName} ${if (isIncoming) "получил" else "отправил"} сообщение \"$image\""

    override fun getType(): String {
        return "image"
    }

    override fun getMessageBody(): String {
        return "@${from?.firstName} - отправил фото"
    }
}
