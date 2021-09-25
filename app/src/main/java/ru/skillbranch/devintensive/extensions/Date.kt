package ru.skillbranch.devintensive.extensions

import android.os.Build
import android.text.style.TtsSpan
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.util.*

const val SECOND = 1_000L
const val MINUTE = 60 * SECOND
const val HOUR = 60 * MINUTE
const val DAY = 24 * HOUR

enum class TimeUnits {
    SECOND {
        override fun plural(value: Int) =
            if (value == 1 || value % 10 == 1) "$value секунду"
            else if (value in 2..4 || value % 10 in 2..4) "$value секунды"
            else "$value секунд"
    },
    MINUTE {
        override fun plural(value: Int) =
            if (value == 1 || value % 10 == 1) "$value минуту"
            else if (value in 2..4 || value % 10 in 2..4) "$value минуты"
            else "$value минут"
    },
    HOUR {
        override fun plural(value: Int) =
            if (value == 1 || value % 10 == 1) "$value час"
            else if (value in 2..4 || value % 10 in 2..4) "$value часа"
            else "$value часов"
    },
    DAY {
        override fun plural(value: Int) =
            if (value == 1 || value % 10 == 1) "1 день"
            else if (value in 2..4 || value % 10 in 2..4) "$value дня"
            else "$value дней"
    };

    abstract fun plural(value: Int): String
}


fun Date.format(pattern: String = "HH:mm:ss dd.MM.yy"): String {
    val sdf = SimpleDateFormat(pattern, Locale("ru", "RU"))
    val timeZoneOffset = sdf.timeZone.getOffset(this.time)

    return sdf.format(this.time.minus(timeZoneOffset))
}

fun Date.add(value: Int, units: TimeUnits = TimeUnits.SECOND): Date {

    this.time += when (units) {
        TimeUnits.SECOND -> value * SECOND
        TimeUnits.MINUTE -> value * MINUTE
        TimeUnits.HOUR -> value * HOUR
        TimeUnits.DAY -> value * DAY
    }

    return this
}

fun Date.humanizeDiff(date: Date = Date()): String {
    var diffTime = (this.time - date.time) / 1_000

    if (diffTime < 0) {
        diffTime *= -1

        return when (diffTime) {
            in 0..1 -> "только что"
            in 2..45 -> "несколько секунд назад"
            in 46..75 -> "минуту назад"
            in 76..2700 -> "${TimeUnits.MINUTE.plural((diffTime / 60).toInt())} назад"
            in 2701..4500 -> "час назад"
            in 4501..79200 -> "${TimeUnits.HOUR.plural((diffTime / 3600).toInt())} назад"
            in 79201..93600 -> "день назад"
            in 93601..3110400 -> "${TimeUnits.DAY.plural((diffTime / 86400).toInt())} назад"
            else -> "более года назад"
        }
    } else {
        return when (diffTime) {
            in 0..1 -> "только что"
            in 2..45 -> "через несколько секунд"
            in 46..75 -> "через минуту"
            in 76..2700 -> "через ${TimeUnits.MINUTE.plural((diffTime / 60).toInt())}"
            in 2701..4500 -> "через час"
            in 4501..79200 -> "через ${TimeUnits.HOUR.plural((diffTime / 3600).toInt())}"
            in 79201..93600 -> "через день"
            in 93601..3110400 -> "через ${TimeUnits.DAY.plural((diffTime / 86400).toInt())}"
            else -> "более чем через год"
        }
    }
}