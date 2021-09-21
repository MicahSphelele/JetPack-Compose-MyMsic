package com.sphe.models.constants

import android.content.ContentUris
import android.content.Context
import android.os.Build
import android.provider.MediaStore
import androidx.core.net.toUri
import com.sphe.models.R
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.*

object Utils {
    const val MUSIC_ONLY_SELECTION = "${MediaStore.Audio.AudioColumns.IS_MUSIC}=1 AND ${MediaStore.Audio.AudioColumns.TITLE} != ''"
    const val EMPTY_ALBUM_ART_URI = "android.resource://com.naman14.timberx/drawable/icon"

    fun getAlbumArtUri(albumId: Long) = ContentUris.withAppendedId(
        "content://media/external/audio/albumart".toUri(), albumId)

    fun makeShortTimeString(context: Context, secs: Long): String {
        var seconds = secs

        val hours: Long = seconds / 3600
        seconds %= 3600
        val minutes: Long = seconds / 60
        seconds %= 60

        val formatString = if (hours == 0L) {
            R.string.durationformatshort
        } else {
            R.string.durationformatlong
        }
        val durationFormat = context.resources.getString(formatString)
        return String.format(durationFormat, hours, minutes, seconds)
    }

    fun isOreo() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O

    fun getIPAddress(useIPv4: Boolean): String {
        try {
            val interfaces = Collections.list(NetworkInterface.getNetworkInterfaces())
            interfaces.forEach { netInterface ->
                val addresses = netInterface.inetAddresses
                while (addresses.hasMoreElements()) {
                    val address = addresses.nextElement()
                    if (!address.isLoopbackAddress) {
                        val result = processAddress(useIPv4, address)
                        if (result != null) return result
                    }
                }
            }
        } catch (_: Exception) {
        }

        return ""
    }

    private fun processAddress(useIPv4: Boolean, address: InetAddress): String? {
        val hostAddress = address.hostAddress
        val isIPv4 = hostAddress!!.indexOf(':') < 0

        if (useIPv4) {
            if (isIPv4) {
                return hostAddress
            }
        } else {
            if (!isIPv4) {
                val endIndex = hostAddress.indexOf('%') // drop ip6 zone suffix
                return if (endIndex < 0) {
                    hostAddress.uppercase(Locale.getDefault())
                } else {
                    hostAddress.substring(0, endIndex).uppercase(Locale.getDefault())
                }
            }
        }
        return null
    }


}