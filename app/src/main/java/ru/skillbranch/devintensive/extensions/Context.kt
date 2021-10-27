package ru.skillbranch.devintensive.extensions

import android.content.Context

/**
 * Created by Oleksiy Pasmarnov on 22.10.21
 */

fun Context.dpToPx(dp:Int): Float {
    return dp.toFloat() * this.resources.displayMetrics.density
}