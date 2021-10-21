package ru.skillbranch.devintensive.repositories

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import ru.skillbranch.devintensive.App
import ru.skillbranch.devintensive.models.Profile

/**
 * Created by Oleksiy Pasmarnov on 12.10.21
 */
object PreferencesRepository {
    private const val FIRST_NAME = "FIRST_NAME"
    private const val LAST_NAME = "LAST_NAME"
    private const val ABOUT = "ABOUT"
    private const val REPOSITORY = "REPOSITORY"
    private const val RATING = "RATING"
    private const val RESPECT = "RESPECT"
    private const val APP_THEME = "APP_THEME"

    private val pref: SharedPreferences by lazy {
        val ctx = App.applicationContext()
        PreferenceManager.getDefaultSharedPreferences(ctx)
    }

    fun saveAppTheme(theme: Int) {
        putValue(APP_THEME to theme)
    }

    fun getAppTheme(): Int = pref.getInt(APP_THEME, 0)

    fun getProfile(): Profile = Profile(
        pref.getString(FIRST_NAME, "")!!,
        pref.getString(LAST_NAME, "")!!,
        pref.getString(ABOUT, "")!!,
        pref.getString(REPOSITORY, "")!!,
        pref.getInt(RATING, 0),
        pref.getInt(RESPECT, 0),
    )

    fun saveProfile(profile: Profile) {
        with(profile) {
            putValue(FIRST_NAME to firstName)
            putValue(LAST_NAME to lastName)
            putValue(ABOUT to about)
            putValue(REPOSITORY to repository)
            putValue(RATING to rating)
            putValue(RESPECT to respect)
        }
    }

    private fun putValue(pair: Pair<String, Any>) = with(pref.edit()) {
        val key = pair.first

        when (val value = pair.second) {
            is Boolean -> putBoolean(key, value)
            is String -> putString(key, value)
            is Int -> putInt(key, value)
            is Float -> putFloat(key, value)
            is Long -> putLong(key, value)
            else -> error("Only primitives types can be stored in Shared Preferences")
        }

        apply()
    }

}