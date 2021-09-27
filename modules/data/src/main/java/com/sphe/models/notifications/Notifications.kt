package com.sphe.models.notifications

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.support.v4.media.session.MediaSessionCompat
import javax.inject.Inject

interface Notifications {

    fun updateNotification(mediaSession: MediaSessionCompat)

    fun buildNotification(mediaSession: MediaSessionCompat): Notification
}

class RealNotifications @Inject constructor(private val context: Context,
                                            private val notificationManager: NotificationManager
) : Notifications {
    override fun updateNotification(mediaSession: MediaSessionCompat) {
        TODO("Not yet implemented")
    }

    override fun buildNotification(mediaSession: MediaSessionCompat): Notification {
        TODO("Not yet implemented")
    }

}