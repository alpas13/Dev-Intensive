package ru.skillbranch.devintensive.models

import java.util.*

class UserView(
    val id: String,
    val fullName: String,
    val nickName: String?,
    var avatar: String? = null,
    var status: String? = "offline",
    val initials: String?,
) {
}