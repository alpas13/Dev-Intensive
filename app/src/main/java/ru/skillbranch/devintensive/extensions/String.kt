package ru.skillbranch.devintensive.extensions

fun String.truncate(length: Int = 16): String {
    return if (this.trim().length > length) {
        return "${this.substring(0, length).trim()}..."
    } else this.trim()
}

fun String.stripHtml(): String {
    return this.run {
        replace("\\A<[&\'\"\\w\\s=>]*".toRegex(), "") }
        .run {
            replace("(?<=[\\w])<.*\\Z".toRegex(), "") }
        .split(" ")
        .filter { it.isNotBlank() }
        .joinToString(" ")
}
