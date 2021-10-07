package ru.skillbranch.devintensive.utils

object Utils {
    fun parseFullName(fullName: String?): Pair<String?, String?> {
        val parts: List<String>? = fullName?.run {
            if (this == "" || this == " ") null else this
        }?.split(" ")

        val firstName = parts?.getOrNull(0)
        val lastName = parts?.getOrNull(1)

        return firstName to lastName
    }

    fun toInitials(firstName: String?, lastName: String?): String? {
        val firstLetter: Char? =
            if (firstName != null && firstName.isNotEmpty() && firstName != " ") {
                firstName.first().uppercaseChar()
            } else null
        val lastLetter: Char? =
            if (lastName != null && lastName.isNotEmpty() && lastName != " ") {
                lastName.first().uppercaseChar()
            } else null

        return when {
            firstLetter != null && lastLetter != null -> "$firstLetter$lastLetter"
            firstLetter != null -> "$firstLetter"
            lastLetter != null -> "$lastLetter"
            else -> null
        }
    }

    fun transliteration(name: String, divider: String = " "): String {
        val fullName: MutableList<String> = mutableListOf()

        name.split(" ").map { fullName.add(it) }

        for (it in fullName.indices) {

            val word: MutableList<String> = mutableListOf()

            for (letter in fullName[it]) {

                if (!letter.isLowerCase()) {
                    val translitLetter: String? = TRANSLITERATION_RU_LAT[letter.lowercaseChar()]

                    if (translitLetter != null) {
                        if (translitLetter.length > 1)
                            word.add("${translitLetter[0].uppercase()}${translitLetter[1]}")
                        else word.add(translitLetter.uppercase())
                    } else word.add(letter.uppercase())
                } else
                    word.add(TRANSLITERATION_RU_LAT[letter.lowercaseChar()] ?: letter.toString())


            }
            fullName[it] = word.joinToString("")
        }

        return fullName.joinToString(divider)
    }

    private val TRANSLITERATION_RU_LAT: Map<Char, String> = mapOf(
        'а' to "a",
        'б' to "b",
        'в' to "v",
        'г' to "g",
        'д' to "d",
        'е' to "e",
        'ё' to "e",
        'ж' to "zh",
        'з' to "z",
        'и' to "i",
        'й' to "i",
        'к' to "k",
        'л' to "l",
        'м' to "m",
        'н' to "n",
        'о' to "o",
        'п' to "p",
        'р' to "r",
        'с' to "s",
        'т' to "t",
        'у' to "u",
        'ф' to "f",
        'х' to "h",
        'ц' to "c",
        'ч' to "ch",
        'ш' to "sh",
        'щ' to "sh'",
        'ъ' to "",
        'ы' to "i",
        'ь' to "",
        'э' to "e",
        'ю' to "yu",
        'я' to "ya",
    )
}