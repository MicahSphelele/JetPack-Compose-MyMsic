package com.sphe.models.extension

import android.database.Cursor
import android.support.v4.media.session.MediaSessionCompat

fun Int.normalizeTrackNumber(): Int {
    var returnValue = this
    // This fixes bug where some track numbers displayed as 100 or 200.
    while (returnValue >= 1000) {
        // When error occurs the track numbers have an extra 1000 or 2000 added, so decrease till normal.
        returnValue -= 1000
    }
    return returnValue
}

fun Cursor?.forEach(
    closeAfter: Boolean = false,
    each: Cursor.() -> Unit
) {
    if (this == null) return
    if (moveToFirst()) {
        do {
            each(this)
        } while (moveToNext())
    }
    if (closeAfter) {
        close()
    }
}

fun <T> Cursor?.mapList(
    closeAfter: Boolean = false,
    mapper: Cursor.() -> T
): MutableList<T> {
    val result = mutableListOf<T>()
    forEach(closeAfter = closeAfter) {
        result.add(mapper(this))
    }
    return result
}

inline fun <reified T> Cursor.value(name: String): T {
    val index = getColumnIndexOrThrow(name)
    return when (T::class) {
        Short::class -> getShort(index) as T
        Int::class -> getInt(index) as T
        Long::class -> getLong(index) as T
        Boolean::class -> (getInt(index) == 1) as T
        String::class -> getString(index) as T
        Float::class -> getFloat(index) as T
        Double::class -> getDouble(index) as T
        ByteArray::class -> getBlob(index) as T
        else -> throw IllegalStateException("What do I do with ${T::class.java.simpleName}?")
    }
}

inline fun <reified T> Cursor.valueOrDefault(name: String, defaultValue: T): T {
    val index = getColumnIndex(name)
    if (index == -1) {
        return defaultValue
    }
    return when (T::class) {
        Short::class -> getShort(index) as? T ?: defaultValue
        Int::class -> getInt(index) as? T ?: defaultValue
        Long::class -> getLong(index) as? T ?: defaultValue
        Boolean::class -> (getInt(index) == 1) as T
        String::class -> getString(index) as? T ?: defaultValue
        Float::class -> getFloat(index) as? T ?: defaultValue
        Double::class -> getDouble(index) as? T ?: defaultValue
        ByteArray::class -> getBlob(index) as? T ?: defaultValue
        else -> throw IllegalStateException("What do I do with ${T::class.java.simpleName}?")
    }
}

fun Cursor.valueOrEmpty(name: String): String = valueOrDefault(name, "")

fun List<MediaSessionCompat.QueueItem>?.toIDList(): LongArray {
    return this?.map { it.queueId }?.toLongArray() ?: LongArray(0)
}

fun timeAddZeros(number: Int?, ifZero: String = ""): String {
    return when (number) {
        0 -> ifZero
        1, 2, 3, 4, 5, 6, 7, 8, 9 -> "0$number"
        else -> number.toString()
    }
}

fun String?.orNA() = when (this.isNullOrEmpty()) {
    false -> this
    else -> "N/A"
}

fun String?.orBlank() = when (this == null) {
    false -> this
    else -> "N/A"
}

fun List<String?>.interpunctize(interpunct: String = " ??? ") = joinToString(interpunct)

fun String?.isNotNullandNotBlank() = this != null && this.isNotBlank()

fun CharSequence.truncate(limit: Int, ellipsize: String = "..."): CharSequence {
    if (length > limit) {
        return substring(0, limit) + ellipsize
    }
    return this
}

fun Long.millisToDuration(): String {
    val seconds = (this / 1000).toInt() % 60
    val minutes = (this / (1000 * 60) % 60).toInt()
    val hours = (this / (1000 * 60 * 60) % 24).toInt()
    "${timeAddZeros(hours)}:${timeAddZeros(minutes, "0")}:${timeAddZeros(seconds, "00")}".apply {
        return if (startsWith(":")) replaceFirst(":", "") else this
    }
}


