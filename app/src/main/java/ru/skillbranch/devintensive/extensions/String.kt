package ru.skillbranch.devintensive.extensions

fun String.truncate(length: Int = 16): String {
    return if (this.trim().length > length) {
        return "${this.substring(0, length).trim()}..."
    } else this.trim()
}

fun String.stripHtml(): String {
    return this
        .run {
            replace("<[-/&\'\"\\w\\s=>]*".toRegex(), " ")
        }
        .run {
            replace("&[ltg]+;[-;\\/&'\"\\w\\s=>]+&[ltg]+;".toRegex(), " ")
        }
        .split(" ")
        .filter { it.isNotBlank() }
        .joinToString(" ")
}
